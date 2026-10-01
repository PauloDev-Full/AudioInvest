package dio.projeto;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OpenAiTranscriptionModelIT {
    @Autowired
    OpenAiAudioTranscriptionModel openAiTranscriptionModel;

    @ParameterizedTest
    @CsvSource({
            "audio-cripto-bitcoin.m4a, 250 reais",
            "audio-investimento-acoes.m4a, 500 reais",
            "audio-investimento-cdb.m4a, 1500 reais",
    })
    public void should_containExpectedKeywords_when_audioFilesAreProcessed(String fileName, String expectedKeyword) {
    	var recording = new org.springframework.core.io.FileSystemResource(
    	        "C:/User/eclipse-workspace/conclusao/lib/src/test/java/dio/audio/" + fileName
    	);

        var response = openAiTranscriptionModel.call(recording);

        assertThat(response).contains(expectedKeyword);
        System.out.println(response);
    }
}
//teste reexecutado para gravar no banco
//Forçando o JUnit a reexecutar o teste do zero
