package com.example.decifraappescolhaumtema.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.decifraappescolhaumtema.*
import com.example.decifraappescolhaumtema.data.model.Usuario
import com.example.decifraappescolhaumtema.ui.viewmodel.AuthViewModel
import com.example.decifraappescolhaumtema.ui.viewmodel.JogoViewModel
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Inicial,
        modifier = modifier
    ) {
        composable<Inicial> {
            TelaInicial(
                onJogarClick = {
                    navController.navigate(Login)
                }
            )
        }

        composable<Login> {
            val authViewModel: AuthViewModel = viewModel()
            TelaLogin(
                onEntrarClick = {
                    navController.navigate(Instrucoes) {
                        popUpTo<Login> { inclusive = true }
                    }
                },
                onCriarContaClick = {
                    navController.navigate(CriarConta)
                },
                authViewModel = authViewModel
            )
        }

        composable<CriarConta> {
            val authViewModel: AuthViewModel = viewModel()
            TelaCriarConta(
                onContaCriadaClick = {
                    navController.navigate(Login) {
                        popUpTo<CriarConta> { inclusive = true }
                    }
                },
                authViewModel = authViewModel
            )
        }

        composable<Instrucoes> {
            TelaInstrucoes(
                onContinuarClick = {
                    navController.navigate(EscolherTema)
                },
                // Callback adicionado para o botão no canto superior direito
                onUsuariosClick = {
                    navController.navigate(Usuarios)
                }
            )
        }

        composable<EscolherTema> {
            TelaEscolhaTema(
                onTemaSelecionado = { temaEscolhido ->
                    navController.navigate(Jogo(tema = temaEscolhido))
                }
            )
        }

        composable<Jogo> { backStackEntry ->
            val args = backStackEntry.toRoute<Jogo>()
            val jogoViewModel: JogoViewModel = viewModel()

            TelaJogo(
                tema = args.tema,
                jogoViewModel = jogoViewModel,
                onJogoFinalizado = { palavraCerta, pontosGanhos, venceu ->
                    navController.navigate(
                        Acerto(palavra = palavraCerta, pontos = pontosGanhos, venceu = venceu)
                    ) {
                        popUpTo<Jogo> { inclusive = true }
                    }
                }
            )
        }

        composable<Acerto> { backStackEntry ->
            val rota = backStackEntry.toRoute<Acerto>()

            TelaAcerto(
                palavra = rota.palavra,
                pontos = rota.pontos,
                venceu = rota.venceu,
                onReiniciarClick = {
                    navController.navigate(EscolherTema) {
                        popUpTo<EscolherTema> { inclusive = true }
                    }
                }
            )
        }

        // Nova rota adicionada sem alterar a estrutura das demais
        composable<Usuarios> {
            val coroutineScope = rememberCoroutineScope()
            var listaUsuarios by remember { mutableStateOf(emptyList<Usuario>()) }

            LaunchedEffect(Unit) {
                // Substitua pela chamada ao seu DAO:
                // listaUsuarios = usuarioDao.buscarTodos()
            }

            TelaUsuarios(
                usuarios = listaUsuarios,
                onNovoUsuarioClick = {
                    navController.navigate(CriarConta) // Exemplo: reaproveita a tela de criar conta
                },
                onAtualizarListaClick = {
                    coroutineScope.launch {
                        // Substitua pela chamada ao seu DAO:
                        // listaUsuarios = usuarioDao.buscarTodos()
                    }
                },
                onAlterarClick = { usuarioSelecionado ->
                    // Lógica para ir para a tela de alteração
                },
                onExcluirClick = { usuarioSelecionado ->
                    coroutineScope.launch {
                        // Substitua pela chamada ao seu DAO:
                        // usuarioDao.deletar(usuarioSelecionado)
                        // listaUsuarios = usuarioDao.buscarTodos()
                    }
                }
            )
        }
    }
}