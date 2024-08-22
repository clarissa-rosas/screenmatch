package br.com.alura.screenmatch.service;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import okhttp3.OkHttpClient;

// Outras importações...

public class ConsultaChatGPT {

    private static final OpenAiService openAiService;

    static {
        String apiKey = System.getenv("OPENAI_APIKEY");
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("A API Key do OpenAI não foi configurada.");
        }

        OkHttpClient httpClient = SSLConfig.createHttpClientWithSsl();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openai.com/v1/")
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        OpenAiApi api = retrofit.create(OpenAiApi.class);

        openAiService = new OpenAiService(api);
    }

    public static String obterTraducao(String sinopse) {
        CompletionRequest requisicao = CompletionRequest.builder()
                .model("text-davinci-003")
                .prompt("Traduza para o português o texto: " + sinopse)
                .maxTokens(5000)
                .temperature(0.7)
                .build();

        try {
            var resposta = openAiService.createCompletion(requisicao);
            System.out.println("Resposta do OpenAI: " + resposta);
            return resposta.getChoices().get(0).getText();
        } catch (Exception e) {
            System.err.println("Erro ao obter tradução do ChatGPT: " + e.getMessage());
            e.printStackTrace();
            return "Erro ao obter tradução.";
        }
    }
}
