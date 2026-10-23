package com.example.decifraappescolhaumtema

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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TelaAlterarUsuario(
    nome: String,
    senha: String,
    carregando: Boolean,
    mensagem: String?,
    onNomeChange: (String) -> Unit,
    onSenhaChange: (String) -> Unit,
    onSalvarClick: () -> Unit,
    onVoltarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF4E9E0))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Alterar usuário",
            fontSize = 36.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(48.dp))

        OutlinedTextField(
            value = nome,
            onValueChange = { if (it.length <= 100) onNomeChange(it) },
            placeholder = { Text(text = "Usuário", color = Color(0xFFA0A0A0)) },
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

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = senha,
            onValueChange = { if (it.length <= 128) onSenhaChange(it) },
            placeholder = { Text(text = "Nova senha (opcional)", color = Color(0xFFA0A0A0)) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
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
            text = "Deixe a senha em branco para manter a atual",
            color = Color(0xFFA0A0A0),
            fontSize = 12.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        )

        mensagem?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = Color(0xFFEF224A))
        }
        if (carregando) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Aguarde...", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onSalvarClick,
            enabled = !carregando,
            modifier = Modifier
                .width(220.dp)
                .height(55.dp)
                .border(2.dp, Color.Black, RoundedCornerShape(28.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1E88A8),
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(
                text = "Salvar",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        TextButton(onClick = onVoltarClick, enabled = !carregando) {
            Text("Cancelar")
        }
    }
}
