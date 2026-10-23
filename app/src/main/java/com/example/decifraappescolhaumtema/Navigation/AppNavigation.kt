package com.example.decifraappescolhaumtema.Navigation

import android.database.sqlite.SQLiteConstraintException
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.decifraappescolhaumtema.*
import com.example.decifraappescolhaumtema.auth.AuthManager
import com.example.decifraappescolhaumtema.auth.SessaoUsuario
import com.example.decifraappescolhaumtema.data.dal.RankingUsuario
import com.example.decifraappescolhaumtema.data.dal.UsuarioDao
import com.example.decifraappescolhaumtema.data.model.Usuario
import com.example.decifraappescolhaumtema.ui.viewmodel.AuthViewModel
import com.example.decifraappescolhaumtema.ui.viewmodel.JogoViewModel
import com.example.decifraappescolhaumtema.validation.CadastroValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                },
                onRankingClick = {
                    navController.navigate(Ranking)
                }
            )
        }

        composable<Ranking> { backStackEntry ->
            val context = LocalContext.current
            val usuarioDao = remember { UsuarioDao(context) }
            var ranking by remember { mutableStateOf(emptyList<RankingUsuario>()) }

            LaunchedEffect(Unit) {
                ranking = withContext(Dispatchers.IO) { usuarioDao.listarRanking() }
            }

            TelaRanking(
                ranking = ranking,
                usuarioAtualId = SessaoUsuario.usuarioId ?: -1L,
                onReiniciarClick = {
                    navController.navigate(EscolherTema) {
                        popUpTo<EscolherTema> { inclusive = true }
                    }
                }
            )
        }

        composable<Usuarios> { backStackEntry ->
            val context = LocalContext.current
            val usuarioDao = remember { UsuarioDao(context) }
            val coroutineScope = rememberCoroutineScope()
            var listaUsuarios by remember { mutableStateOf(emptyList<Usuario>()) }

            val lifecycle = backStackEntry.lifecycle
            DisposableEffect(lifecycle) {
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_RESUME) {
                        coroutineScope.launch {
                            listaUsuarios = withContext(Dispatchers.IO) { usuarioDao.listar() }
                        }
                    }
                }
                lifecycle.addObserver(observer)
                onDispose { lifecycle.removeObserver(observer) }
            }

            TelaUsuarios(
                usuarios = listaUsuarios,
                onNovoUsuarioClick = {
                    navController.navigate(CriarConta(origemUsuarios = true))
                },
                onAtualizarListaClick = {
                    coroutineScope.launch {
                        listaUsuarios = withContext(Dispatchers.IO) { usuarioDao.listar() }
                    }
                },
                onAlterarClick = { usuarioSelecionado ->
                    navController.navigate(AlterarUsuario(id = usuarioSelecionado.id))
                },
                onExcluirClick = { usuarioSelecionado ->
                    coroutineScope.launch {
                        withContext(Dispatchers.IO) {
                            usuarioDao.excluir(usuarioSelecionado.id, SessaoUsuario.usuarioId ?: -1L)
                        }
                        listaUsuarios = withContext(Dispatchers.IO) { usuarioDao.listar() }
                    }
                }
            )
        }

        composable<AlterarUsuario> { backStackEntry ->
            val args = backStackEntry.toRoute<AlterarUsuario>()
            val context = LocalContext.current
            val usuarioDao = remember { UsuarioDao(context) }
            val coroutineScope = rememberCoroutineScope()

            var nome by remember { mutableStateOf("") }
            var senha by remember { mutableStateOf("") }
            var carregando by remember { mutableStateOf(false) }
            var mensagem by remember { mutableStateOf<String?>(null) }

            LaunchedEffect(args.id) {
                val usuario = withContext(Dispatchers.IO) { usuarioDao.buscarPorId(args.id) }
                if (usuario != null) nome = usuario.nome
            }

            TelaAlterarUsuario(
                nome = nome,
                senha = senha,
                carregando = carregando,
                mensagem = mensagem,
                onNomeChange = { nome = it },
                onSenhaChange = { senha = it },
                onSalvarClick = {
                    if (!carregando) {
                        val nomeLimpo = nome.trim()
                        val senhaLimpa = senha.trim()
                        mensagem = null
                        carregando = true
                        coroutineScope.launch {
                            try {
                                withContext(Dispatchers.IO) {
                                    if (senhaLimpa.isEmpty()) {
                                        require(nomeLimpo.length in 3..100) {
                                            "Informe um usuário entre 3 e 100 caracteres."
                                        }
                                        usuarioDao.alterar(args.id, nomeLimpo)
                                    } else {
                                        CadastroValidator.validar(nomeLimpo, senhaLimpa)
                                        val salt = AuthManager.criarSalt()
                                        val hash = AuthManager.gerarHash(senhaLimpa, salt)
                                        try {
                                            usuarioDao.alterar(args.id, nomeLimpo, hash, salt)
                                        } finally {
                                            hash.fill(0)
                                        }
                                    }
                                }
                                navController.popBackStack()
                            } catch (e: SQLiteConstraintException) {
                                mensagem = "Usuário já cadastrado."
                            } catch (e: IllegalArgumentException) {
                                mensagem = e.message
                            } catch (e: Exception) {
                                mensagem = "Ocorreu um erro inesperado."
                            } finally {
                                carregando = false
                            }
                        }
                    }
                },
                onVoltarClick = { navController.popBackStack() }
            )
        }
    }
}