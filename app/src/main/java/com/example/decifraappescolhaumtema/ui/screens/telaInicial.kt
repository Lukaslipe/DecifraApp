package com.example.decifraappescolhaumtema.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TelaInicial(
    onJogarClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
            .background(Color(0xFFF4E9E0)),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        LogoDecifra()


        Spacer(modifier = Modifier.height(100.dp))

        Button(
            onClick = onJogarClick,
            modifier = Modifier
                .width(250.dp)
                .height(65.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF58B914),
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(35.dp)
        ) {
            Text(
                text = "Jogar",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun LogoDecifra() {

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        LetraLogo("D", Color(0xFF827875))
        LetraLogo("E", Color(0xFF827875))
        LetraLogo("C", Color(0xFF827875))
        LetraLogo("I", Color(0xFFFFC800))
        LetraLogo("F", Color(0xFF827875))
        LetraLogo("R", Color(0xFF55B91A))
        LetraLogo("A", Color(0xFF827875))

    }
}

@Composable
fun LetraLogo(
    letra: String,
    cor: Color
) {

    Box(
        modifier = Modifier
            .size(48.dp)
            .background(
                color = cor,
                shape = RoundedCornerShape(5.dp)
            ),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = letra,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}
