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
import com.example.decifraappescolhaumtema.ui.viewmodel.PalavraViewModel
import com.example.decifraappescolhaumtema.ui.viewmodel.UsuarioViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // Estado local para gerir as categorias dinamicamente
    var listaCategoriasState by remember {
        mutableStateOf(
            mutableListOf(
                CategoriaModel(1, "Comida"),
                CategoriaModel(2, "Animais"),
                CategoriaModel(3, "Bebidas"),
                CategoriaModel(4, "Planetas")
            )
        )
    }

    // Uma única instância (escopo da Activity) para a lista e a criação de palavras compartilharem o estado
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
            TelaCategoriasCustom(
                categorias = listaCategoriasState,
                onNovaCategoriaClick = {
                    navController.navigate(CriarCategoria)
                },
                onAtualizarListaClick = {
                    // Ação de atualizar/recarregar se necessário
                },
                onExcluirClick = { cat ->
                    listaCategoriasState = listaCategoriasState.filter { it.id != cat.id }.toMutableList()
                },
                onAlterarClick = { cat ->
                    navController.navigate(AlterarCategoria(id = cat.id, nomeAtual = cat.nome))
                }
            )
        }

        composable<CriarCategoria> {
            TelaCriarCategoria(
                onCategoriaCriada = { novoNome ->
                    val novaId = (listaCategoriasState.maxOfOrNull { it.id } ?: 0) + 1
                    listaCategoriasState = (listaCategoriasState + CategoriaModel(novaId, novoNome)).toMutableList()
                    navController.popBackStack()
                }
            )
        }

        composable<AlterarCategoria> { backStackEntry ->
            val args = backStackEntry.toRoute<AlterarCategoria>()
            TelaAlterarCategoria(
                id = args.id,
                nomeAtual = args.nomeAtual,
                onSalvarAlteracao = { idCat, novoNome ->
                    listaCategoriasState = listaCategoriasState.map {
                        if (it.id == idCat) it.copy(nome = novoNome) else it
                    }.toMutableList()
                    navController.popBackStack()
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
            TelaAlterarPalavras(
                palavras = palavraViewModel.palavrasList,
                onNovaPalavraClick = {
                    navController.navigate(CriarPalavra)
                },
                onAtualizarListaClick = {
                    palavraViewModel.carregarPalavras()
                },
                onAlterarClick = { },
                onExcluirClick = { id ->
                    palavraViewModel.excluirPalavra(id)
                }
            )
        }

        composable<CriarPalavra> {
            TelaCriarPalavra(
                palavra = palavraViewModel.palavra,
                onPalavraChange = palavraViewModel::mudarPalavra,
                categoria = palavraViewModel.categoria,
                onCategoriaChange = palavraViewModel::mudarCategoria,
                categoriasDisponiveis = palavraViewModel.categoriasList,
                onCriarClick = {
                    palavraViewModel.salvarPalavra()
                    navController.popBackStack()
                },
                onVoltarClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}