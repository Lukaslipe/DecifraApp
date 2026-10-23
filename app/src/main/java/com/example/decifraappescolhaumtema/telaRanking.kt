package com.example.decifraappescolhaumtema

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.decifraappescolhaumtema.ui.viewmodel.UsuarioViewModel

@Composable
fun TelaRanking(
    viewModel: UsuarioViewModel,
    onReiniciarClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) { viewModel.carregarRanking() }

    val ranking = viewModel.ranking
    val usuarioAtualId = viewModel.usuarioAtivoId ?: -1L
    val posicaoAtual = ranking.indexOfFirst { it.id == usuarioAtualId }
    val usuarioAtual = ranking.getOrNull(posicaoAtual)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF4E9E0))
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(90.dp)
                    .background(Color(0xFFE2D6CB), RoundedCornerShape(4.dp))
                    .border(1.dp, Color(0xFFC8BCB2), RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.BottomStart
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.img_estrelas),
                    contentDescription = "Estrelas",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(44.dp)
                        .offset(x = (-16).dp, y = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = usuarioAtual?.nome ?: "-",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = "Posição: ${if (posicaoAtual >= 0) (posicaoAtual + 1).toString().padStart(2, '0') else "--"}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Pontos: ${(usuarioAtual?.pontos ?: 0).toString().padStart(2, '0')}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            if (ranking.isEmpty()) {
                Text(
                    text = "Nenhum jogador no ranking ainda.",
                    fontSize = 16.sp,
                    color = Color(0xFF555555)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    itemsIndexed(ranking) { index, usuario ->
                        CardItemRanking(
                            nome = usuario.nome,
                            posicao = index + 1,
                            pontos = usuario.pontos,
                            destaque = usuario.id == usuarioAtualId
                        )
                    }
                }
            }
        }

        Button(
            onClick = onReiniciarClick,
            modifier = Modifier
                .width(200.dp)
                .height(50.dp)
                .border(2.dp, Color.Black, RoundedCornerShape(25.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEBC110),
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(25.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.refresh_svgrepo_com),
                contentDescription = "Reiniciar",
                modifier = Modifier.size(26.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun CardItemRanking(
    nome: String,
    posicao: Int,
    pontos: Int,
    destaque: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (destaque) Color(0xFFD8E8C8) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(width = 50.dp, height = 36.dp)
                .background(Color(0xFFE2D6CB), RoundedCornerShape(2.dp))
                .border(1.dp, Color(0xFFC8BCB2), RoundedCornerShape(2.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = posicao.toString().padStart(2, '0'),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = nome,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "Posição: ${posicao.toString().padStart(2, '0')}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF555555)
            )
        }

        Text(
            text = "${pontos.toString().padStart(2, '0')} pts",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}
