package com.example.decifraappescolhaumtema

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.decifraappescolhaumtema.ui.viewmodel.EstadoJogo
import com.example.decifraappescolhaumtema.ui.viewmodel.JogoViewModel
import com.example.decifraappescolhaumtema.ui.viewmodel.Resultado

val FundoBege = Color(0xFFF4E9E0)
val FundoGridLetra = Color(0xFFF0F3FA)
val BordaGridLetra = Color(0xFFC8CDD8)
val CorLinhaAzul = Color(0xFF1389A7)
val CorLinhaVerde = Color(0xFF67B82D)
val CorLinhaAmarela = Color(0xFFE8B614)
val CorLinhaVermelha = Color(0xFFDF1D44)

val CorCerta = Color(0xFF67B82D)
val CorPresente = Color(0xFFE8B614)
val CorAusente = Color(0xFF787C7E)

@Composable
fun TelaJogo(
    tema: String = "",
    jogoViewModel: JogoViewModel,
    onJogoFinalizado: (String, Int, Boolean) -> Unit = { _, _, _ -> },
    modifier: Modifier = Modifier
) {
    LaunchedEffect(tema) {
        jogoViewModel.iniciar(tema)
    }

    LaunchedEffect(jogoViewModel.estado) {
        when (jogoViewModel.estado) {
            EstadoJogo.VENCEU -> onJogoFinalizado(jogoViewModel.palavraCerta, jogoViewModel.pontos, true)
            EstadoJogo.PERDEU -> onJogoFinalizado(jogoViewModel.palavraCerta, 0, false)
            EstadoJogo.JOGANDO -> Unit
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FundoBege)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = jogoViewModel.tema,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        GridPalavras(
            tentativas = jogoViewModel.tentativas,
            resultados = jogoViewModel.resultados,
            linhaAtual = jogoViewModel.linhaAtual
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            jogoViewModel.mensagem?.let {
                Text(text = it, color = CorLinhaVermelha, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
            }

            TecladoCustom(
                onTeclaPressionada = { jogoViewModel.digitar(it) },
                onApagar = { jogoViewModel.apagar() },
                onConfirmar = { jogoViewModel.confirmar() }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun GridPalavras(
    tentativas: List<List<Char>>,
    resultados: List<List<Resultado>>,
    linhaAtual: Int
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        tentativas.forEachIndexed { linha, letras ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                letras.forEachIndexed { coluna, letra ->
                    val resultado = resultados[linha][coluna]
                    val corFundo = when (resultado) {
                        Resultado.CERTA -> CorCerta
                        Resultado.PRESENTE -> CorPresente
                        Resultado.AUSENTE -> CorAusente
                        Resultado.VAZIA -> if (letra != ' ') Color.White else FundoGridLetra
                    }
                    val corTexto = if (resultado == Resultado.VAZIA) Color.Black else Color.White

                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(color = corFundo, shape = RoundedCornerShape(8.dp))
                            .border(
                                width = 1.dp,
                                color = if (linha == linhaAtual && letra != ' ' && resultado == Resultado.VAZIA)
                                    Color.Black else BordaGridLetra,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = letra.toString(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = corTexto
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TecladoCustom(
    onTeclaPressionada: (Char) -> Unit,
    onApagar: () -> Unit,
    onConfirmar: () -> Unit
) {
    val fileira1 = listOf('Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P')
    val fileira2 = listOf('A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L')
    val fileira3 = listOf('Z', 'X', 'C', 'V', 'B', 'N', 'M')

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            fileira1.forEach { letra ->
                TeclaItem(letra = letra, corFundo = CorLinhaAzul, onClick = { onTeclaPressionada(letra) })
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            fileira2.forEach { letra ->
                TeclaItem(letra = letra, corFundo = CorLinhaVerde, onClick = { onTeclaPressionada(letra) })
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TeclaAcao(texto = "ENTER", corFundo = CorLinhaVerde, onClick = onConfirmar)
            fileira3.forEach { letra ->
                TeclaItem(letra = letra, corFundo = CorLinhaAmarela, onClick = { onTeclaPressionada(letra) })
            }
            TeclaAcao(texto = "⌫", corFundo = CorLinhaVermelha, onClick = onApagar)
        }
    }
}

@Composable
fun TeclaItem(
    letra: Char,
    corFundo: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(width = 32.dp, height = 46.dp)
            .background(color = corFundo, shape = RoundedCornerShape(6.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letra.toString(),
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TeclaAcao(
    texto: String,
    corFundo: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(width = 52.dp, height = 46.dp)
            .background(color = corFundo, shape = RoundedCornerShape(6.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
