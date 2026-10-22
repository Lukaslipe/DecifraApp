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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val FundoBege = Color(0xFFF4E9E0)
val FundoGridLetra = Color(0xFFF0F3FA)
val BordaGridLetra = Color(0xFFC8CDD8)
val CorLinhaAzul = Color(0xFF1389A7)
val CorLinhaVerde = Color(0xFF67B82D)
val CorLinhaAmarela = Color(0xFFE8B614)
val CorLinhaVermelha = Color(0xFFDF1D44)

@Composable
fun TelaJogo(
    tema: String = "",
    onJogoFinalizado: (String, Int) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    val tentativas = remember { mutableStateOf(List(6) { List(5) { ' ' } }) }
    val linhaAtual = remember { mutableStateOf(0) }
    val colunaAtual = remember { mutableStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FundoBege)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = tema,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        GridPalavras(
            tentativas = tentativas.value,
            linhaAtual = linhaAtual.value
        )

        TecladoCustom(
            onTeclaPressionada = { letra ->
                if (colunaAtual.value < 5) {
                    val novaLinha = tentativas.value[linhaAtual.value].toMutableList()
                    novaLinha[colunaAtual.value] = letra
                    val novaTentativas = tentativas.value.toMutableList()
                    novaTentativas[linhaAtual.value] = novaLinha
                    tentativas.value = novaTentativas
                    colunaAtual.value++

                    if (colunaAtual.value == 5) {
                        val palavra = tentativas.value[linhaAtual.value].joinToString("")
                        onJogoFinalizado(palavra, 5)
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun GridPalavras(
    tentativas: List<List<Char>>,
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
                letras.forEach { letra ->
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(
                                color = if (letra != ' ') Color.White else FundoGridLetra,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = if (linha == linhaAtual && letra != ' ')
                                    Color.Black else BordaGridLetra,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = letra.toString(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TecladoCustom(onTeclaPressionada: (Char) -> Unit) {
    val linhas = listOf(
        Pair(listOf('Q', 'W', 'E', 'R', 'T', 'Y', 'U'), CorLinhaAzul),
        Pair(listOf('I', 'O', 'P', 'A', 'S', 'D', 'F'), CorLinhaVerde),
        Pair(listOf('G', 'H', 'J', 'K', 'L', 'Z', 'X'), CorLinhaAmarela),
        Pair(listOf('C', 'V', 'B', 'N', 'M'), CorLinhaVermelha)
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        linhas.forEach { (letras, corFundo) ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                letras.forEach { letra ->
                    TeclaItem(
                        letra = letra,
                        corFundo = corFundo,
                        onClick = { onTeclaPressionada(letra) }
                    )
                }
            }
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
            .size(width = 40.dp, height = 44.dp)
            .background(
                color = corFundo,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letra.toString(),
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
