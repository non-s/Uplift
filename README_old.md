# 📱 Frases Motivacionais - App Android

App Android de frases motivacionais com Firebase e monetização via AdMob.

## ✨ Funcionalidades

- ✅ Lista de frases motivacionais organizadas por categorias
- ⭐ Sistema de favoritos
- 🔄 Compartilhamento social
- 📊 Firebase Firestore (database em nuvem)
- 💰 Monetização com AdMob (banners + anúncios intersticiais)
- 🔔 Notificações diárias automáticas
- 📱 Interface moderna com Material Design

## 🚀 Como Configurar

### 1. Configurar Firebase

1. Acesse o [Firebase Console](https://console.firebase.google.com/)
2. Clique em "Adicionar projeto" e siga os passos
3. Após criar o projeto, clique em "Adicionar app" e selecione Android
4. Preencha os dados:
   - **Nome do pacote**: `com.motivacional.frases`
   - **Apelido do app**: Frases Motivacionais
   - **SHA-1**: (opcional para este projeto)
5. Baixe o arquivo `google-services.json`
6. **IMPORTANTE**: Copie o arquivo `google-services.json` para a pasta `app/` do projeto

### 2. Ativar Serviços do Firebase

No Firebase Console:

1. **Firestore Database**:
   - No menu lateral, vá em "Firestore Database"
   - Clique em "Criar banco de dados"
   - Escolha o modo de "Produção"
   - Selecione a localização (southamerica-east1 para Brasil)

2. **Regras de Segurança do Firestore**:
   ```
   rules_version = '2';
   service cloud.firestore {
     match /databases/{database}/documents {
       match /quotes/{quote} {
         allow read: if true;
         allow write: if false;
       }
     }
   }
   ```
   (Permite leitura pública, escrita apenas via console)

3. **Cloud Messaging** (para notificações):
   - Já está ativado por padrão ao criar o projeto

### 3. Configurar AdMob

1. Acesse o [AdMob Console](https://apps.admob.com/)
2. Clique em "Apps" > "Adicionar app"
3. Preencha:
   - **Nome do app**: Frases Motivacionais
   - **Plataforma**: Android
4. Anote o **ID do app** que será gerado
5. Crie dois blocos de anúncios:
   - **Banner**: para o rodapé da tela
   - **Intersticial**: para exibir entre ações

6. **Substituir IDs de teste por IDs reais**:

   Em `app/src/main/AndroidManifest.xml`:
   ```xml
   <meta-data
       android:name="com.google.android.gms.ads.APPLICATION_ID"
       android:value="SEU_ID_DO_APP_ADMOB"/>
   ```

   Em `app/src/main/res/values/strings.xml`:
   ```xml
   <string name="banner_ad_unit_id">SEU_ID_BANNER</string>
   <string name="interstitial_ad_unit_id">SEU_ID_INTERSTICIAL</string>
   ```

### 4. Popular Database com Frases

Após configurar tudo:

1. Abra o app
2. No menu superior (três pontos), clique em "Inicializar Database"
3. Isso vai adicionar 10 frases iniciais ao Firestore

### 5. Adicionar Mais Frases

Você pode adicionar frases diretamente no Firebase Console:

1. Acesse Firestore Database
2. Clique em "Adicionar coleção"
3. Nome da coleção: `quotes`
4. Adicione documentos com os campos:
   - `text` (string): Texto da frase
   - `author` (string): Autor
   - `category` (string): motivation, success, love, life ou wisdom
   - `timestamp` (number): Timestamp atual

## 📂 Estrutura do Projeto

```
app/
├── data/
│   ├── model/          # Modelos de dados (Quote, Category)
│   └── repository/     # QuoteRepository (comunicação com Firebase)
├── ui/
│   ├── adapters/       # QuoteAdapter (RecyclerView)
│   ├── viewmodel/      # QuoteViewModel (lógica de negócio)
│   └── MainActivity    # Tela principal
├── services/           # Firebase Messaging Service
└── utils/              # Helpers (AdMob, Favoritos, Notificações)
```

## 💰 Estratégia de Monetização

### Anúncios Banner
- Exibido permanentemente no rodapé da tela
- Menor CPM, mas sempre visível

### Anúncios Intersticiais
- Exibido a cada 5 ações do usuário:
  - Favoritar uma frase
  - Compartilhar uma frase
- Maior CPM, mas menos frequente

### Dicas para Maximizar Receita

1. **Publique no Google Play**
2. **Promova o app**:
   - Redes sociais (Instagram, TikTok, Facebook)
   - Grupos de WhatsApp/Telegram
   - Crie conteúdo sobre motivação
3. **Atualize regularmente**:
   - Adicione novas frases semanalmente
   - Mantenha o conteúdo fresco
4. **Engajamento**:
   - Incentive compartilhamento
   - Use notificações diárias (mas sem exagero)
5. **Otimize para ASO** (App Store Optimization):
   - Título descritivo
   - Palavras-chave: frases, motivação, inspiração
   - Screenshots atraentes

## 🔧 Compilar e Executar

1. Abra o projeto no Android Studio
2. Aguarde o Gradle sincronizar
3. Conecte um dispositivo Android ou use emulador
4. Clique em Run (▶️)

## 📊 Próximas Funcionalidades (Ideias)

- [ ] Widget para tela inicial
- [ ] Modo escuro
- [ ] Busca de frases
- [ ] Categorias personalizadas
- [ ] Login com Google (salvar favoritos na nuvem)
- [ ] Compartilhamento como imagem
- [ ] Estatísticas de uso

## 📝 Notas Importantes

- **Teste os anúncios**: Use os IDs de teste durante desenvolvimento
- **Políticas do AdMob**: Nunca clique nos próprios anúncios
- **Conteúdo**: Adicione frases originais e de domínio público
- **Privacidade**: Adicione política de privacidade antes de publicar

## 📄 Licença

Este projeto é livre para uso pessoal e comercial.

---

**Desenvolvido com Claude Code** 🤖
