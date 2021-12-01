package br.unipar.plano.domain.contratos.usecases.impl


import br.unipar.plano.domain.centrais.usecases.impl.CentralNotFoundException
import br.unipar.plano.domain.contratos.model.Contrato
import br.unipar.plano.domain.contratos.model.ContratoRepository
import br.unipar.plano.domain.contratos.model.StatusContrato
import br.unipar.plano.domain.contratos.model.factories.CONTRATO_CO_ID
import br.unipar.plano.domain.contratos.model.factories.contrato
import br.unipar.plano.domain.contratos.model.factories.idContrato
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

private val ID_CONTRATO_INEXISTENTE = idContrato(false)

class CancelaContratoUseCaseImplTesk {

    private val contratoRepository = mock<ContratoRepository>()
    private val cancelaContratoUseCase = CancelaContratoUseCaseImpl(contratoRepository)

    private val argumentCaptor = argumentCaptor<Contrato>()

    @BeforeEach
    fun setUp() {
        whenever(contratoRepository.findById(eq(CONTRATO_CO_ID))).thenReturn(Optional.of(contrato().cancela()))
        whenever(contratoRepository.findById(eq(ID_CONTRATO_INEXISTENTE))).thenReturn(Optional.empty())
    }

    @Test
    fun `deve cancelar o contrato informado`() {
        cancelaContratoUseCase.executa(CONTRATO_CO_ID)

        verify(contratoRepository).save(argumentCaptor.capture())
        val contratoSalva = argumentCaptor.firstValue

        Assertions.assertEquals(StatusContrato.CANCELADO, contratoSalva.status)
    }

    @Test
    fun `deve disparar uma excecao se o contrato nao existir`() {
        assertThrows<CentralNotFoundException> {
            cancelaContratoUseCase.executa(ID_CONTRATO_INEXISTENTE)
        }

        verify(contratoRepository, never()).save(any())
    }
}