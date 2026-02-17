# Uplift — App de Frases Motivacionais Android

Aplicativo Android para frases motivacionais diarias com temas customizaveis, favoritos, notificacoes agendadas, widget para tela inicial e monetizacao via AdMob.

---

## Visao Geral

O Uplift exibe frases de um banco local (Room/SQLite) alimentado pelo Firebase Firestore. O usuario recebe uma frase diferente por dia via notificacao, pode salvar favoritas, pesquisar por texto/autor, personalizar o tema visual e compartilhar frases como imagem.

---

## Stack Tecnica

| Camada | Tecnologia |
|---|---|
| Linguagem | Kotlin 2.0.0 |
| UI | Jetpack Compose + Material Design 3 + View System |
| Arquitetura | MVVM |
| Banco local | Room 2.8.3 (SQLite) |
| Banco remoto | Firebase Firestore |
| Push | Firebase Cloud Messaging (FCM) |
| Background | WorkManager 2.11.0 + AlarmManager |
| Async | Kotlin Coroutines + LiveData |
| Widget | Glance AppWidget 1.1.1 |
| Monetizacao | Google AdMob 24.7.0 |
| Preferencias | DataStore + SharedPreferences |
| Splash | SplashScreen API (core-splashscreen 1.2.0) |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 34 |
| Compile SDK | 36 |
| JVM Target | Java 17 |

---

## Arquitetura

```
app/src/main/java/com/motivacional/frases/
│
├── data/
│   ├── model/
│   │   ├── Quote.kt              # Modelo de frase (texto, autor, categoria, id)
│   │   └── Category.kt           # Enum de categorias (MOTIVACAO, SUCESSO, AMOR, VIDA, SABEDORIA)
│   ├── local/
│   │   ├── QuoteDatabase.kt      # Room Database (v1)
│   │   ├── QuoteEntity.kt        # Entidade Room — frases
│   │   ├── QuoteDao.kt           # DAO — busca por categoria, texto e autor
│   │   ├── FavoriteQuoteEntity.kt# Entidade Room — frases favoritadas
│   │   └── FavoriteQuoteDao.kt   # DAO — insert/delete/getAll favoritos
│   ├── repository/
│   │   └── QuoteRepository.kt    # Orquestra Room + Firestore
│   └── QuotesData.kt             # Banco estatico de frases (fallback offline)
│
├── ui/
│   ├── MainActivity.kt           # Entry point — Compose + ViewBinding
│   ├── SearchActivity.kt         # Tela de busca por texto/autor
│   ├── SettingsActivity.kt       # Configuracoes: tema, fonte, notificacoes
│   ├── adapters/
│   │   └── QuoteAdapter.kt       # RecyclerView adapter para listagem de frases
│   ├── viewmodel/
│   │   ├── QuoteViewModel.kt     # Estado principal: frase atual, categoria, favoritos
│   │   └── ThemeViewModel.kt     # Estado do tema (cor, modo claro/escuro)
│   └── theme/
│       ├── Theme.kt              # MaterialTheme com 5 paletas de cores
│       ├── Color.kt              # Definicao das cores dos 5 temas
│       └── Type.kt               # Tipografia Material 3
│
├── utils/
│   ├── AdMobHelper.kt            # Inicializacao e exibicao de banners AdMob
│   ├── DailyQuoteAlarmManager.kt # Agenda alarme diario via AlarmManager
│   ├── DailyQuoteReceiver.kt     # BroadcastReceiver — dispara notificacao diaria
│   ├── DailyQuoteWorker.kt       # WorkManager Worker — alternativa ao AlarmManager
│   ├── BootReceiver.kt           # Reagenda alarme apos reinicializacao do dispositivo
│   ├── FavoritesManager.kt       # Utilitarios para gerenciar favoritos
│   ├── PreferencesManager.kt     # Wrapper SharedPreferences (tema, fonte, horario)
│   ├── ThemeHelper.kt            # Aplica tema na Activity
│   ├── ShareHelper.kt            # Intent de compartilhamento de texto
│   └── QuoteImageGenerator.kt    # Gera Bitmap com frase para compartilhamento visual
│
├── widget/
│   └── QuoteWidget.kt            # Glance AppWidget — exibe frase do dia na home screen
│
└── services/
    └── MyFirebaseMessagingService.kt  # FCM — recebe tokens e notificacoes remotas
```

---

## Fluxo de Dados

```
Firebase Firestore
        ↓  sincronizacao
   Room Database  ←── QuotesData.kt (dados estaticos / fallback)
        ↓
  QuoteRepository
        ↓  LiveData / Flow
  QuoteViewModel
        ↓  collectAsStateWithLifecycle / observeAsState
MainActivity (Compose)
```

---

## Categorias de Frases

| Categoria | Enum |
|---|---|
| Motivacao | `MOTIVACAO` |
| Sucesso | `SUCESSO` |
| Amor | `AMOR` |
| Vida | `VIDA` |
| Sabedoria | `SABEDORIA` |

O usuario pode navegar entre categorias na tela principal. A categoria selecionada filtra frases no `QuoteViewModel`.

---

## Sistema de Notificacoes

Duas estrategias de agendamento implementadas:

**AlarmManager (principal)**
```
DailyQuoteAlarmManager.schedule()
    → AlarmManager.setRepeating() com horario configuravel
    → DailyQuoteReceiver.onReceive()
    → NotificationManager.notify()
```

**WorkManager (alternativo)**
```
DailyQuoteWorker : CoroutineWorker
    → WorkManager.enqueueUniquePeriodicWork()
    → exibe notificacao via NotificationManager
```

`BootReceiver` garante que o alarme seja reagendado apos reinicializacao do dispositivo (`BOOT_COMPLETED`).

---

## Temas Visuais

5 paletas de cores configuradas em `Theme.kt` e `Color.kt`:

1. **Roxo** (padrao)
2. **Azul**
3. **Verde**
4. **Laranja**
5. **Rosa**

Cada tema suporta modo **claro** e **escuro**. A preferencia e salva via `PreferencesManager` (SharedPreferences) e restaurada na inicializacao pelo `ThemeViewModel`.

---

## Widget (Home Screen)

Implementado com **Jetpack Glance**:

- Exibe frase do dia diretamente na tela inicial do Android
- Atualizado pelo `DailyQuoteWorker`
- Abre o app ao clicar

---

## Monetizacao

Integrado com **Google AdMob**:

- `AdMobHelper` inicializa o SDK do AdMob na Application
- Banners sao exibidos nas principais telas
- IDs de anuncio configurados em `strings.xml` (substituir pelos IDs de producao)

---

## Compartilhamento

Duas modalidades:

- **Texto**: `ShareHelper` dispara um `Intent.ACTION_SEND` com o texto da frase
- **Imagem**: `QuoteImageGenerator` renderiza a frase em um `Bitmap` com fundo gradiente e retorna via `Intent` de compartilhamento

---

## Configuracao para Rodar

1. Crie um projeto no [Firebase Console](https://console.firebase.google.com)
2. Adicione um app Android com package `com.motivacional.frases`
3. Baixe o `google-services.json` e coloque em `app/`
4. Configure o AdMob em `app/src/main/res/values/strings.xml`
5. Abra no Android Studio e execute em um dispositivo Android 7+

```bash
# Build debug
./gradlew assembleDebug

# Build release (requer keystore configurado)
./gradlew assembleRelease
```

---

## Estrutura de Arquivos Raiz

```
Uplift/
├── app/
│   ├── src/main/
│   │   ├── java/com/motivacional/frases/  # Codigo-fonte (descrito acima)
│   │   └── res/                            # Resources Android
│   │       ├── layout/                     # XMLs de layout (View system)
│   │       ├── drawable/                   # Gradientes e icones vetoriais
│   │       ├── anim/                       # Animacoes de transicao
│   │       ├── values/                     # Cores, strings, temas
│   │       └── xml/                        # Configuracoes de widget e backup
│   ├── build.gradle
│   └── proguard-rules.pro
├── gradle/
│   └── wrapper/
├── build.gradle
├── settings.gradle
├── gradle.properties
├── screenshot.png
└── Documentation/                          # Guias de configuracao e publicacao
    ├── README.md
    ├── COMECE_AQUI.md
    ├── GUIA_FIREBASE.md
    ├── COMPILAR_SEM_ANDROID_STUDIO.md
    ├── PUBLICAR.md
    └── GUIA_PUBLICACAO_COMPLETO.md
```
