package com.example.decifraappescolhaumtema.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.decifraappescolhaumtema.data.model.Categoria

@Composable
fun TelaCategoriasCustom(
    categorias: List<Categoria>,
    mensagem: String? = null,
    carregando: Boolean = false,
    onNovaCategoriaClick: () -> Unit = {},
    onAtualizarListaClick: () -> Unit = {},
    onAlterarClick: (Categoria) -> Unit = {},
    onExcluirClick: (Categoria) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var categoriaParaExcluir by remember { mutableStateOf<Categoria?>(null) }

    categoriaParaExcluir?.let { categoria ->
        AlertDialog(
            onDismissRequest = { categoriaParaExcluir = null },
            title = { Text("Excluir categoria") },
            text = {
                Text(
                    "Excluir \"${categoria.nome}\"? " +
                            "As ${categoria.totalPalavras} palavras dessa categoria também serão excluídas."
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    categoriaParaExcluir = null
                    onExcluirClick(categoria)
                }) { Text("Excluir", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { categoriaParaExcluir = null }) { Text("Cancelar") }
            }
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF4E9E0))
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Categorias",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF007E99)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onNovaCategoriaClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .shadow(4.dp, RoundedCornerShape(25.dp))
                        .border(2.dp, Color(0xFFD4A017), RoundedCornerShape(25.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC800)),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text(
                        text = "Nova Categoria",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Button(
                    onClick = onAtualizarListaClick,
                    enabled = !carregando,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .shadow(4.dp, RoundedCornerShape(25.dp))
                        .border(2.dp, Color(0xFF449A12), RoundedCornerShape(25.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF58B914)),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text(
                        text = "Atualizar Lista",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            mensagem?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = Color(0xFFF2385A))
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (categorias.isEmpty() && !carregando) {
                Text(
                    text = "Nenhuma categoria cadastrada.",
                    fontSize = 16.sp,
                    color = Color(0xFF555555)
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = categorias,
                    key = { it.id }
                ) { categoria ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(2.dp, RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEBE2DA)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = categoria.nome,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )

                            Text(
                                text = if (categoria.totalPalavras == 1) "1 palavra"
                                else "${categoria.totalPalavras} palavras",
                                fontSize = 13.sp,
                                color = Color(0xFF555555)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Text(
                                    text = "Alterar",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Red,
                                    modifier = Modifier.clickable { onAlterarClick(categoria) }
                                )

                                Text(
                                    text = "Excluir",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Red,
                                    modifier = Modifier.clickable { categoriaParaExcluir = categoria }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
