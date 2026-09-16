package dio.projeto.infrastructure.http;

import dio.projeto.application.ListInvestmentsByCategoryUseCase;
import dio.projeto.application.PersistInvestmentUseCase;
import dio.projeto.domain.Category;
import dio.projeto.infrastructure.http.request.InvestmentRequest;
import dio.projeto.infrastructure.http.response.InvestmentResponse;
import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/investments")
public class InvestmentController {

    private final PersistInvestmentUseCase persistInvestmentUseCase;
    private final ListInvestmentsByCategoryUseCase listInvestmentsByCategoryUseCase;

    private final TranscriptionModel transcriptionModel;
    private final ChatClient chatClient;
    private final TextToSpeechModel textToSpeechModel;

    public InvestmentController(PersistInvestmentUseCase persistInvestmentUseCase,
                                ListInvestmentsByCategoryUseCase listInvestmentsByCategoryUseCase,
                                TranscriptionModel transcriptionModel,
                                @Value("classpath:prompts/system-message.st") Resource systemPrompt,
                                ChatClient.Builder chatClientBuilder,
                                TextToSpeechModel textToSpeechModel) throws IOException {

        this.persistInvestmentUseCase = persistInvestmentUseCase;
        this.listInvestmentsByCategoryUseCase = listInvestmentsByCategoryUseCase;
        this.transcriptionModel = transcriptionModel;
        this.chatClient = chatClientBuilder

                .defaultSystem(systemPrompt.getContentAsString(StandardCharsets.UTF_8))
                .defaultTools(persistInvestmentUseCase, listInvestmentsByCategoryUseCase)
                .build();
        this.textToSpeechModel = textToSpeechModel;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InvestmentResponse createInvestment(@RequestBody InvestmentRequest request) {
        var investment = persistInvestmentUseCase.execute(request.toInput());
        return InvestmentResponse.from(investment);
    }

    @GetMapping("/{category}")
    public List<InvestmentResponse> readInvestments(@PathVariable Category category) {
        return listInvestmentsByCategoryUseCase.execute(category)
                .stream()
                .map(InvestmentResponse::from)
                .toList();
    }

    @PostMapping(value = "/ai",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = "audio/mpeg")
    public ResponseEntity<Resource> transcribe(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "arquivo de audio vazio");
        }

        var userMessage = transcriptionModel.transcribe(file.getResource());
        var result = chatClient.prompt().user(userMessage).call().content();

        if (result == null || result.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "o modelo nao retornou texto");
        }

        byte[] audio = textToSpeechModel.call(result);
        var resource = new ByteArrayResource(audio);

        return ResponseEntity.ok()
                .contentLength(audio.length)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("audio.mp3")
                                .build()
                                .toString())
                .body(resource);
    }
}