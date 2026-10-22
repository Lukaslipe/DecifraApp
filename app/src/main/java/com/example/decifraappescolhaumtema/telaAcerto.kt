package com.example.decifraappescolhaumtema

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TelaAcerto(
    palavra: String = "CARGA",
    pontos: Int = 5,
    venceu: Boolean = true,
    onReiniciarClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF4E9E0))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (venceu) "Acertou!" else "Não foi dessa vez!",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "A palavra correta é",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            palavra.forEach { letra ->
                LetraAcerto(letra = letra.toString())
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        if (venceu) {
            Box(
                modifier = Modifier
                    .size(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_estrelas),
                    contentDescription = "Estrelas de vitória",
                    modifier = Modifier.size(120.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Você acumulou\n$pontos pontos!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        } else {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Tente novamente!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onReiniciarClick,
            modifier = Modifier
                .width(220.dp)
                .height(60.dp)
                .border(2.dp, Color.Black, RoundedCornerShape(30.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEBC110),
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(30.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.refresh_svgrepo_com),
                contentDescription = "Reiniciar",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun LetraAcerto(letra: String) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(
                color = Color(0xFF6BB825),
                shape = RoundedCornerShape(8.dp)
            )
            .border(1.dp, Color(0xFF4A8518), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letra,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTelaAcerto() {
    TelaAcerto()
}