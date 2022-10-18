package br.com.alura.leilao.service;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Pagamento;
import br.com.alura.leilao.model.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;

class GeradorDePagamentosTest {

    private GeradorDePagamento geradorDePagamento;

    @Mock
    private PagamentoDao pagamentoDao;

    @Captor
    private ArgumentCaptor<Pagamento> pagamentoCaptor;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        this.geradorDePagamento = new GeradorDePagamento(pagamentoDao);
    }

    @Test
    void deveriaCriarPagamentoVencedorLeilao() {
        Leilao leilao = buildLeilao();
        Lance lanceVencedor = leilao.getLances().get(0);
        geradorDePagamento.gerarPagamento(lanceVencedor);
        Mockito.verify(pagamentoDao).salvar(pagamentoCaptor.capture());
        Pagamento pagamento = pagamentoCaptor.getValue();
        Assertions.assertEquals(LocalDate.now().plusDays(1), pagamento.getVencimento());
        Assertions.assertEquals(lanceVencedor.getValor(), pagamento.getValor());
        Assertions.assertFalse(pagamento.getPago());
        Assertions.assertEquals(lanceVencedor.getUsuario(), pagamento.getUsuario());
        Assertions.assertEquals(leilao, pagamento.getLeilao());
    }

    private Leilao buildLeilao() {
        Leilao leilao = new Leilao("Celular", new BigDecimal("500"), new Usuario("Fulano"));
        Lance segundo = new Lance(new Usuario("Ciclano"), new BigDecimal("900"));
        leilao.propoe(segundo);
        return leilao;
    }

}
