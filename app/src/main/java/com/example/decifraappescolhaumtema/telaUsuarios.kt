package com.example.decifraappescolhaumtema

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.decifraappescolhaumtema.data.model.Usuario

@Composable
fun TelaUsuarios(
    usuarios: List<Usuario>,
    onNovoUsuarioClick: () -> Unit = {},
    onAtualizarListaClick: () -> Unit = {},
    onAlterarClick: (Usuario) -> Unit = {},
    onExcluirClick: (Usuario) -> Unit = {},
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF4E9E0)) // Cor de fundo do app
            .padding(24.dp)
    ) {
        Text(
            text = "Usuários",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF007E99) // Azul do título
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onNovoUsuarioClick,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                    .height(48.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(24.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEBC110), // Amarelo
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(text = "Novo Usuário", fontSize = 14.sp)
            }

            Button(
                onClick = onAtualizarListaClick,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
                    .height(48.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(24.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6BB825), // Verde
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(text = "Atualizar Lista", fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(usuarios) { usuario ->
                ItemUsuario(
                    usuario = usuario,
                    onAlterarClick = { onAlterarClick(usuario) },
                    onExcluirClick = { onExcluirClick(usuario) }
                )
            }
        }
    }
}

@Composable
fun ItemUsuario(
    usuario: Usuario,
    onAlterarClick: () -> Unit,
    onExcluirClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFE4E6F1),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(20.dp)
    ) {
        // Mostra o nome do usuário
        Text(
            text = usuario.nome,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        // APAGUE a parte do usuario.email que estava aqui!
        // Se quiser mostrar o ID no lugar, você pode adicionar:
        // Text(text = "ID: ${usuario.id}", fontSize = 16.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Alterar",
                color = Color(0xFFF2385A),
                fontSize = 16.sp,
                modifier = Modifier.clickable { onAlterarClick() }
            )
            Text(
                text = "Excluir",
                color = Color(0xFFF2385A),
                fontSize = 16.sp,
                modifier = Modifier.clickable { onExcluirClick() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTelaUsuarios() {
    // Lista de exemplo com a estrutura real (id e nome) para o Preview funcionar sem erro
    TelaUsuarios(
        usuarios = listOf(
            Usuario(id = 1L, nome = "elo"),
            Usuario(id = 2L, nome = "eloisa")
        )
    )
}