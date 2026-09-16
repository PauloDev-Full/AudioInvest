package dio.projeto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnabledIfEnvironmentVariable(named = "Investment", matches = ".+")
public class OpenAiChatModelIT {
    @Autowired
    OpenAiApi openAiApi;

    @Test
    void should_receiveResponse_when_chatModelIsCalled() {
        var options = OpenAiChatOptions.builder()
                .model("gpt-4o-mini")
                .temperature(0.8)
                .responseFormat(ResponseFormat.builder().type(ResponseFormat.Type.TEXT).build())
                .build();

        var chatModel = OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(options)
                .build();

        var response = chatModel.call("Gere um registro de investimento, com descrição de aporte, valor em reais e banco ou/e tipo de investimento");

        assertThat(response).isNotEmpty();
        System.out.println(response);
    }
}