package com.example.decifraappescolhaumtema.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class PalavraItem(val id: Long, val texto: String, val categoria: String = "")

@Composable
fun TelaAlterarPalavras(
    palavras: List<PalavraItem>,
    onNovaPalavraClick: () -> Unit,
    onAtualizarListaClick: () -> Unit,
    onAlterarClick: (Long) -> Unit,
    onExcluirClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF4E9E0))
            .padding(24.dp)
    ) {
        Text(
            text = "Palavras",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E88A8)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onNovaPalavraClick,
                modifier = Modifier
                    .height(45.dp)
                    .border(1.5.dp, Color.Black, RoundedCornerShape(20.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFD54F),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(text = "Nova Palavra", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = onAtualizarListaClick,
                modifier = Modifier
                    .height(45.dp)
                    .border(1.5.dp, Color.Black, RoundedCornerShape(20.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7CB342),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(text = "Atualizar Lista", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(palavras) { palavra ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE5E9F0)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = palavra.texto,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            TextButton(onClick = { onAlterarClick(palavra.id) }) {
                                Text(
                                    text = "Alterar",
                                    color = Color(0xFFEF224A),
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            TextButton(onClick = { onExcluirClick(palavra.id) }) {
                                Text(
                                    text = "Excluir",
                                    color = Color(0xFFEF224A),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TelaAlterarPalavrasPreview() {
    val listaExemplo = listOf(
        PalavraItem(1, "Amigo"),
        PalavraItem(2, "Porta"),
        PalavraItem(3, "Mundo"),
        PalavraItem(4, "Saber")
    )

    TelaAlterarPalavras(
        palavras = listaExemplo,
        onNovaPalavraClick = {},
        onAtualizarListaClick = {},
        onAlterarClick = {},
        onExcluirClick = {}
    )
}