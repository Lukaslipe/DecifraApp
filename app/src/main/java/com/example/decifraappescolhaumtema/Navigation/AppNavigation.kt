package com.example.decifraappescolhaumtema.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.decifraappescolhaumtema.*

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
            TelaLogin(
                onEntrarClick = {
                    navController.navigate(Instrucoes)
                },
                onCriarContaClick = {
                    navController.navigate(CriarConta)
                }
            )
        }

        composable<CriarConta> {
            TelaCriarConta(
                onContaCriadaClick = {
                    navController.navigate(Login) {
                        popUpTo<CriarConta> { inclusive = true }
                    }
                }
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

            TelaJogo(
                tema = args.tema,
                onJogoFinalizado = { palavraCerta, pontosGanhos ->
                    navController.navigate(Acerto(palavra = palavraCerta, pontos = pontosGanhos)) {
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
                onReiniciarClick = {
                    navController.navigate(EscolherTema) {
                        popUpTo<EscolherTema> { inclusive = true }
                    }
                }
            )
        }
    }
}