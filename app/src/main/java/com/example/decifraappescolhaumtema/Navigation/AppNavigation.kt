package com.example.decifraappescolhaumtema.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.decifraappescolhaumtema.*
import com.example.decifraappescolhaumtema.ui.viewmodel.AuthViewModel
import com.example.decifraappescolhaumtema.ui.viewmodel.JogoViewModel

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
    }
}