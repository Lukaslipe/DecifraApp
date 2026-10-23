package com.example.decifraappescolhaumtema.ui.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TelaInstrucoes(
    onContinuarClick: () -> Unit = {},
    onUsuariosClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF4E9E0))
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(48.dp)
                .background(Color(0xFF007E99), RoundedCornerShape(24.dp))
                .clickable { onUsuariosClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Usuários",
                tint = Color.White
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(140.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Como jogar:",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Descubra a palavra certa em 6 tentativas.\n" +
                        "Depois de cada tentativa, as peças mostram\n" +
                        "o quão perto você está da solução.",
                fontSize = 16.sp,
                color = Color(0xFF555555),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Letra("C", Color(0xFF58B914), Color.White)
                Spacer(modifier = Modifier.width(5.dp))
                Letra("A", Color(0xFFE9E9E9), Color(0xFF333333))
                Spacer(modifier = Modifier.width(5.dp))
                Letra("M", Color(0xFFE9E9E9), Color(0xFF333333))
                Spacer(modifier = Modifier.width(5.dp))
                Letra("P", Color(0xFFE9E9E9), Color(0xFF333333))
                Spacer(modifier = Modifier.width(5.dp))
                Letra("O", Color(0xFFE9E9E9), Color(0xFF333333))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "A letra C faz parte da palavra e está na posição correta.",
                fontSize = 14.sp,
                color = Color(0xFF555555),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Letra("F", Color(0xFFE9E9E9), Color(0xFF333333))
                Spacer(modifier = Modifier.width(5.dp))
                Letra("E", Color(0xFFFFC800), Color.White)
                Spacer(modifier = Modifier.width(5.dp))
                Letra("S", Color(0xFFE9E9E9), Color(0xFF333333))
                Spacer(modifier = Modifier.width(5.dp))
                Letra("T", Color(0xFFE9E9E9), Color(0xFF333333))
                Spacer(modifier = Modifier.width(5.dp))
                Letra("A", Color(0xFFE9E9E9), Color(0xFF333333))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "A letra E faz parte da palavra, mas está em outra posição.",
                fontSize = 14.sp,
                color = Color(0xFF555555),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Letra("P", Color(0xFFE9E9E9), Color(0xFF333333))
                Spacer(modifier = Modifier.width(5.dp))
                Letra("U", Color(0xFFE9E9E9), Color(0xFF333333))
                Spacer(modifier = Modifier.width(5.dp))
                Letra("L", Color(0xFFE9E9E9), Color(0xFF333333))
                Spacer(modifier = Modifier.width(5.dp))
                Letra("G", Color(0xFF4A4A4A), Color.White)
                Spacer(modifier = Modifier.width(5.dp))
                Letra("A", Color(0xFFE9E9E9), Color(0xFF333333))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "A letra G não faz parte da palavra.",
                fontSize = 14.sp,
                color = Color(0xFF555555),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Os acentos são preenchidos automaticamente, e não são considerados nas dicas.",
                fontSize = 14.sp,
                color = Color(0xFF555555),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(80.dp))

            Button(
                onClick = onContinuarClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
                    .shadow(
                        elevation = 5.dp,
                        shape = RoundedCornerShape(35.dp),
                        ambientColor = Color(0xFF555555),
                        spotColor = Color(0xFF555555)
                    )
                    .border(
                        width = 2.dp,
                        color = Color(0xFF449A12),
                        shape = RoundedCornerShape(35.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF58B914),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(35.dp)
            ) {
                Text(
                    text = "Continuar",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun Letra(
    letra: String,
    fundo: Color,
    texto: Color
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(
                color = fundo,
                shape = RoundedCornerShape(5.dp)
            )
            .border(
                width = 1.dp,
                color = Color(0xFFB4B4B4),
                shape = RoundedCornerShape(5.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letra,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = texto
        )
    }
}