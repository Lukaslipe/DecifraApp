package com.example.decifraappescolhaumtema.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.decifraappescolhaumtema.ui.viewmodel.PalavraViewModel
import com.example.decifraappescolhaumtema.validation.CatalogoValidator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCriarPalavra(
    viewModel: PalavraViewModel,
    palavraId: Long? = null,
    onVoltarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val existente = palavraId?.let { id -> viewModel.palavras.firstOrNull { it.id == id } }

    var texto by remember(existente?.id) { mutableStateOf(existente?.texto ?: "") }
    var categoriaId by remember(existente?.id) { mutableStateOf(existente?.categoriaId) }
    var expanded by remember { mutableStateOf(false) }

    val categoriaNome = viewModel.categorias.firstOrNull { it.id == categoriaId }?.nome ?: ""
    val editando = palavraId != null

    LaunchedEffect(Unit) {
        viewModel.limparMensagem()
        viewModel.consumirSalvo()
        viewModel.listar()
    }
    LaunchedEffect(viewModel.salvo) {
        if (viewModel.salvo) {
            viewModel.consumirSalvo()
            onVoltarClick()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF4E9E0))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (editando) "Alterar palavra" else "Criar palavra",
            fontSize = 36.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(48.dp))

        OutlinedTextField(
            value = texto,
            onValueChange = { if (it.length <= CatalogoValidator.TAMANHO_PALAVRA) texto = it },
            enabled = !viewModel.carregando,
            placeholder = { Text(text = "Palavra", color = Color(0xFFA0A0A0)) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF2F5F8),
                unfocusedContainerColor = Color(0xFFF2F5F8),
                focusedBorderColor = Color(0xFFB0BEC5),
                unfocusedBorderColor = Color(0xFFB0BEC5),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "${CatalogoValidator.TAMANHO_PALAVRA} letras. Acentos são removidos automaticamente.",
            color = Color(0xFFA0A0A0),
            fontSize = 12.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { if (!viewModel.carregando) expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = categoriaNome,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text(text = "Selecione a Categoria", color = Color(0xFFA0A0A0)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF2F5F8),
                    unfocusedContainerColor = Color(0xFFF2F5F8),
                    focusedBorderColor = Color(0xFFB0BEC5),
                    unfocusedBorderColor = Color(0xFFB0BEC5),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                viewModel.categorias.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.nome) },
                        onClick = {
                            categoriaId = item.id
                            expanded = false
                        }
                    )
                }
            }
        }

        viewModel.mensagem?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = Color(0xFFEF224A))
        }

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                if (palavraId == null) {
                    viewModel.criar(texto, categoriaId)
                } else {
                    viewModel.alterar(palavraId, texto, categoriaId)
                }
            },
            enabled = !viewModel.carregando && !viewModel.salvo,
            modifier = Modifier
                .width(220.dp)
                .height(55.dp)
                .border(2.dp, Color.Black, RoundedCornerShape(28.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEF224A),
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(
                text = if (editando) "Salvar" else "Criar",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        TextButton(onClick = onVoltarClick, enabled = !viewModel.carregando) {
            Text("Cancelar")
        }
    }
}
