package dio.projeto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class BudgetingApplicationTests {

    @Test
    void contextLoads() {
    }
    @Test
    void testCriarTabelaEGravarDado() {
        System.out.println(">>> FORÇANDO A CRIAÇÃO DA TABELA NO POSTGRESQUE <<<");
       
    }
    
    @Autowired
    private dio.projeto.domain.InvestmentRepository investmentRepository;

    @Test
    void testBuscarInvestimentosDoBanco() {
        System.out.println("=================================================");
        System.out.println(">>> BUSCANDO DADOS DO POSTGRESQL VIA SPRING AI <<<");
        
        java.util.List<dio.projeto.domain.Investment> lista = 
            investmentRepository.findAllByCategory(dio.projeto.domain.Category.EQUITIES);
        
        if (lista.isEmpty()) {
            System.out.println("AVISO: NENHUM DADO ENCONTRADO NA CATEGORIA EQUITIES.");
        } else {
            lista.forEach(inv -> {
                System.out.println("INVESTIMENTO SALVO: " + inv.getDescription() + 
                                   " | VALOR: " + inv.getInvestmentAmount());
            });
        }
        
        System.out.println("=================================================");
    }


    
}

