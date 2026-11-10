package com.example.decifraappescolhaumtema.Navigation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.decifraappescolhaumtema.ui.screens.*
import com.example.decifraappescolhaumtema.ui.viewmodel.AuthViewModel
import com.example.decifraappescolhaumtema.ui.viewmodel.JogoViewModel
import com.example.decifraappescolhaumtema.ui.viewmodel.CategoriaViewModel
import com.example.decifraappescolhaumtema.ui.viewmodel.PalavraViewModel
import com.example.decifraappescolhaumtema.ui.viewmodel.UsuarioViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val categoriaViewModel: CategoriaViewModel = viewModel()
    val palavraViewModel: PalavraViewModel = viewModel()

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
                    navController.navigate(CriarConta())
                },
                authViewModel = authViewModel
            )
        }

        composable<CriarConta> { backStackEntry ->
            val args = backStackEntry.toRoute<CriarConta>()
            val authViewModel: AuthViewModel = viewModel()
            TelaCriarConta(
                onContaCriadaClick = {
                    if (args.origemUsuarios) {
                        navController.navigate(Usuarios) {
                            popUpTo<CriarConta> { inclusive = true }
                        }
                    } else {
                        navController.navigate(Login) {
                            popUpTo<CriarConta> { inclusive = true }
                        }
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
                onEditarMenuClick = {
                    navController.navigate(EditarMenu)
                }
            )
        }

        composable<EditarMenu> {
            TelaEditarMenu(
                onUsuariosClick = {
                    navController.navigate(Usuarios)
                },
                onCategoriasClick = {
                    navController.navigate(Categorias)
                },
                onPalavrasClick = {
                    navController.navigate(AlterarPalavras)
                }
            )
        }

        composable<Categorias> {
            LaunchedEffect(Unit) {
                categoriaViewModel.limparMensagem()
                categoriaViewModel.listar()
            }
            TelaCategoriasCustom(
                categorias = categoriaViewModel.categorias,
                mensagem = categoriaViewModel.mensagem,
                carregando = categoriaViewModel.carregando,
                onNovaCategoriaClick = {
                    navController.navigate(CriarCategoria)
                },
                onAtualizarListaClick = {
                    categoriaViewModel.listar()
                },
                onExcluirClick = { cat ->
                    categoriaViewModel.excluir(cat.id)
                },
                onAlterarClick = { cat ->
                    navController.navigate(AlterarCategoria(id = cat.id, nomeAtual = cat.nome))
                }
            )
        }

        composable<CriarCategoria> {
            TelaCriarCategoria(
                viewModel = categoriaViewModel,
                onVoltarClick = { navController.popBackStack() }
            )
        }

        composable<AlterarCategoria> { backStackEntry ->
            val args = backStackEntry.toRoute<AlterarCategoria>()
            TelaAlterarCategoria(
                id = args.id,
                nomeAtual = args.nomeAtual,
                viewModel = categoriaViewModel,
                onVoltarClick = { navController.popBackStack() }
            )
        }

        composable<EscolherTema> {
            LaunchedEffect(Unit) { categoriaViewModel.listar() }
            TelaEscolhaTema(
                temas = categoriaViewModel.temasJogaveis,
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
                },
                onRankingClick = {
                    navController.navigate(Ranking)
                }
            )
        }

        composable<Ranking> {
            TelaRanking(
                viewModel = viewModel(),
                onReiniciarClick = {
                    navController.navigate(EscolherTema) {
                        popUpTo<EscolherTema> { inclusive = true }
                    }
                }
            )
        }

        composable<Usuarios> {
            TelaUsuarios(
                viewModel = viewModel(),
                onNovoUsuarioClick = {
                    navController.navigate(CriarConta(origemUsuarios = true))
                },
                onAlterarClick = { usuarioSelecionado ->
                    navController.navigate(AlterarUsuario(id = usuarioSelecionado.id))
                }
            )
        }

        composable<AlterarUsuario> { backStackEntry ->
            val args = backStackEntry.toRoute<AlterarUsuario>()

            TelaAlterarUsuario(
                usuarioId = args.id,
                viewModel = viewModel(),
                onVoltarClick = { navController.popBackStack() }
            )
        }

        composable<AlterarPalavras> {
            LaunchedEffect(Unit) {
                palavraViewModel.limparMensagem()
                palavraViewModel.listar()
            }
            TelaAlterarPalavras(
                palavras = palavraViewModel.palavras,
                mensagem = palavraViewModel.mensagem,
                carregando = palavraViewModel.carregando,
                onNovaPalavraClick = {
                    navController.navigate(CriarPalavra)
                },
                onAtualizarListaClick = {
                    palavraViewModel.listar()
                },
                onAlterarClick = { id ->
                    navController.navigate(EditarPalavra(id = id))
                },
                onExcluirClick = { id ->
                    palavraViewModel.excluir(id)
                }
            )
        }

        composable<CriarPalavra> {
            TelaCriarPalavra(
                viewModel = palavraViewModel,
                onVoltarClick = { navController.popBackStack() }
            )
        }

        composable<EditarPalavra> { backStackEntry ->
            val args = backStackEntry.toRoute<EditarPalavra>()
            TelaCriarPalavra(
                viewModel = palavraViewModel,
                palavraId = args.id,
                onVoltarClick = { navController.popBackStack() }
            )
        }
    }
}