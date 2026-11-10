package com.example.decifraappescolhaumtema.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import com.example.decifraappescolhaumtema.ui.viewmodel.CategoriaViewModel

@Composable
fun TelaCriarCategoria(
    viewModel: CategoriaViewModel,
    onVoltarClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var nomeCategoria by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.limparMensagem()
        viewModel.consumirSalvo()
    }
    LaunchedEffect(viewModel.salvo) {
        if (viewModel.salvo) {
            viewModel.consumirSalvo()
            onVoltarClick()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF4E9E0))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Criar categoria",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = nomeCategoria,
                onValueChange = { if (it.length <= 30) nomeCategoria = it },
                enabled = !viewModel.carregando,
                placeholder = { Text("Categoria", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color(0xFF007E99),
                    unfocusedBorderColor = Color(0xFFB4B4B4)
                ),
                singleLine = true
            )

            viewModel.mensagem?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = Color(0xFFEF224A))
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = { viewModel.criar(nomeCategoria) },
                enabled = !viewModel.carregando && !viewModel.salvo,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(50.dp)
                    .shadow(4.dp, RoundedCornerShape(25.dp))
                    .border(2.dp, Color(0xFFD4A017), RoundedCornerShape(25.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC800)),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(
                    text = "Criar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            TextButton(onClick = onVoltarClick, enabled = !viewModel.carregando) {
                Text("Cancelar")
            }
        }
    }
}
