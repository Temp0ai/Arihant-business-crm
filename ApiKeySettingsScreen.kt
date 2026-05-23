package com.teacoffeecrm.ui.settings

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiKeySettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val uriHandler = LocalUriHandler.current
    val clipboardManager = LocalClipboardManager.current

    // Show snackbar on events
    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("API Key Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4E342E),
                    titleContentColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ─── STATUS BANNER ───
            ConfigStatusBanner(isConfigured = uiState.isConfigured)

            // ─── GET KEY GUIDE ───
            GetKeyGuideCard(onOpenUrl = { uriHandler.openUri(it) })

            // ─── GEMINI KEY INPUT ───
            ApiKeyInputCard(
                title = "🤖 Gemini API Key",
                subtitle = "Required for AI messages, captions, keywords",
                value = uiState.geminiKey,
                maskedValue = uiState.maskedGeminiKey,
                isVisible = uiState.showGeminiKey,
                isSaved = uiState.geminiKeySaved,
                isValidating = uiState.isValidatingGemini,
                validationError = uiState.geminiKeyError,
                onValueChange = viewModel::onGeminiKeyChange,
                onToggleVisibility = viewModel::toggleGeminiVisibility,
                onSave = viewModel::saveGeminiKey,
                onClear = viewModel::clearGeminiKey,
                onPasteFromClipboard = {
                    val text = clipboardManager.getText()?.text ?: ""
                    viewModel.onGeminiKeyChange(text)
                },
                keyColor = Color(0xFF1565C0)
            )

            // ─── GMAIL CLIENT ID ───
            ApiKeyInputCard(
                title = "📧 Gmail OAuth Client ID",
                subtitle = "Required for email fetching & lead extraction",
                value = uiState.gmailClientId,
                maskedValue = uiState.maskedGmailId,
                isVisible = uiState.showGmailKey,
                isSaved = uiState.gmailKeySaved,
                isValidating = false,
                validationError = uiState.gmailKeyError,
                onValueChange = viewModel::onGmailClientIdChange,
                onToggleVisibility = viewModel::toggleGmailVisibility,
                onSave = viewModel::saveGmailClientId,
                onClear = viewModel::clearGmailClientId,
                onPasteFromClipboard = {
                    val text = clipboardManager.getText()?.text ?: ""
                    viewModel.onGmailClientIdChange(text)
                },
                keyColor = Color(0xFFE65100)
            )

            // ─── GMB API KEY ───
            ApiKeyInputCard(
                title = "🏪 Google My Business Key",
                subtitle = "Optional — for GMB auto-responder & local SEO",
                value = uiState.gmbKey,
                maskedValue = uiState.maskedGmbKey,
                isVisible = uiState.showGmbKey,
                isSaved = uiState.gmbKeySaved,
                isValidating = false,
                validationError = null,
                onValueChange = viewModel::onGmbKeyChange,
                onToggleVisibility = viewModel::toggleGmbVisibility,
                onSave = viewModel::saveGmbKey,
                onClear = viewModel::clearGmbKey,
                onPasteFromClipboard = {
                    val text = clipboardManager.getText()?.text ?: ""
                    viewModel.onGmbKeyChange(text)
                },
                keyColor = Color(0xFF2E7D32)
            )

            // ─── HUGGING FACE TOKEN ───
            ApiKeyInputCard(
                title = "🤗 Hugging Face Token",
                subtitle = "Optional — for FLUX image generation",
                value = uiState.hfToken,
                maskedValue = uiState.maskedHfToken,
                isVisible = uiState.showHfKey,
                isSaved = uiState.hfKeySaved,
                isValidating = false,
                validationError = null,
                onValueChange = viewModel::onHfTokenChange,
                onToggleVisibility = viewModel::toggleHfVisibility,
                onSave = viewModel::saveHfToken,
                onClear = viewModel::clearHfToken,
                onPasteFromClipboard = {
                    val text = clipboardManager.getText()?.text ?: ""
                    viewModel.onHfTokenChange(text)
                },
                keyColor = Color(0xFF6A1B9A)
            )

            // ─── SAVE ALL BUTTON ───
            Button(
                onClick = viewModel::saveAllKeys,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E342E)),
                enabled = !uiState.isSaving
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Save All Keys", fontSize = 16.sp)
            }

            // ─── TEST CONNECTION ───
            OutlinedButton(
                onClick = viewModel::testGeminiConnection,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.NetworkCheck, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Test Gemini Connection")
            }

            // ─── TEST RESULT ───
            AnimatedVisibility(visible = uiState.testResult != null) {
                TestResultCard(result = uiState.testResult ?: "")
            }

            // ─── SECURITY INFO ───
            SecurityInfoCard()

            // ─── DANGER ZONE ───
            DangerZoneCard(
                onClearAll = viewModel::clearAllKeys
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}

// ─────────────────────────────────────────────
// SUB-COMPONENTS
// ─────────────────────────────────────────────

@Composable
fun ConfigStatusBanner(isConfigured: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isConfigured)
                Color(0xFF2E7D32).copy(alpha = 0.15f)
            else
                Color(0xFFE65100).copy(alpha = 0.15f)
        ),
        border = BorderStroke(
            1.dp,
            if (isConfigured) Color(0xFF2E7D32) else Color(0xFFE65100)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                if (isConfigured) Icons.Default.CheckCircle else Icons.Default.Warning,
                contentDescription = null,
                tint = if (isConfigured) Color(0xFF2E7D32) else Color(0xFFE65100)
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    if (isConfigured) "✅ API Keys Configured" else "⚠️ Setup Required",
                    fontWeight = FontWeight.Bold,
                    color = if (isConfigured) Color(0xFF2E7D32) else Color(0xFFE65100)
                )
                Text(
                    if (isConfigured)
                        "All features are ready to use"
                    else
                        "Add your Gemini API key to enable AI features",
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun GetKeyGuideCard(onOpenUrl: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1565C0).copy(alpha = 0.08f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFF1565C0)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "How to get FREE API Keys?",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1565C0)
                    )
                }
                Icon(
                    if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ApiKeyGuideItem(
                        number = "1",
                        title = "Gemini API Key (Free)",
                        steps = listOf(
                            "Go to → aistudio.google.com",
                            "Sign in with Google account",
                            "Click 'Get API Key'",
                            "Create new project or use existing",
                            "Copy the key starting with 'AIza...'"
                        ),
                        buttonText = "Open Google AI Studio",
                        url = "https://aistudio.google.com",
                        onOpenUrl = onOpenUrl,
                        color = Color(0xFF1565C0)
                    )

                    Divider()

                    ApiKeyGuideItem(
                        number = "2",
                        title = "Gmail OAuth Client ID",
                        steps = listOf(
                            "Go to → console.cloud.google.com",
                            "Create or select a project",
                            "Enable Gmail API",
                            "Go to Credentials → Create OAuth Client",
                            "Choose Android → add your package name",
                            "Copy the Client ID"
                        ),
                        buttonText = "Open Google Cloud Console",
                        url = "https://console.cloud.google.com",
                        onOpenUrl = onOpenUrl,
                        color = Color(0xFFE65100)
                    )

                    Divider()

                    ApiKeyGuideItem(
                        number = "3",
                        title = "Hugging Face Token (Free)",
                        steps = listOf(
                            "Go to → huggingface.co",
                            "Sign up / Sign in",
                            "Click Profile → Settings → Access Tokens",
                            "New Token → Role: Read",
                            "Copy the token starting with 'hf_...'"
                        ),
                        buttonText = "Open Hugging Face",
                        url = "https://huggingface.co/settings/tokens",
                        onOpenUrl = onOpenUrl,
                        color = Color(0xFF6A1B9A)
                    )
                }
            }
        }
    }
}

@Composable
fun ApiKeyGuideItem(
    number: String,
    title: String,
    steps: List<String>,
    buttonText: String,
    url: String,
    onOpenUrl: (String) -> Unit,
    color: Color
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = RoundedCornerShape(50),
                color = color,
                modifier = Modifier.size(24.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(number, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.width(8.dp))
            Text(title, fontWeight = FontWeight.Bold, color = color)
        }

        Spacer(Modifier.height(8.dp))

        steps.forEach { step ->
            Row(
                modifier = Modifier.padding(start = 32.dp, bottom = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.ArrowRight,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = color
                )
                Text(step, fontSize = 13.sp)
            }
        }

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            onClick = { onOpenUrl(url) },
            modifier = Modifier.padding(start = 32.dp),
            border = BorderStroke(1.dp, color)
        ) {
            Icon(
                Icons.Default.OpenInNew,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = color
            )
            Spacer(Modifier.width(4.dp))
            Text(buttonText, color = color, fontSize = 13.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiKeyInputCard(
    title: String,
    subtitle: String,
    value: String,
    maskedValue: String,
    isVisible: Boolean,
    isSaved: Boolean,
    isValidating: Boolean,
    validationError: String?,
    onValueChange: (String) -> Unit,
    onToggleVisibility: () -> Unit,
    onSave: () -> Unit,
    onClear: () -> Unit,
    onPasteFromClipboard: () -> Unit,
    keyColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(title, fontWeight = FontWeight.Bold, color = keyColor)
                    Text(subtitle, fontSize = 12.sp, color = Color.Gray)
                }
                if (isSaved) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Saved",
                        tint = Color(0xFF2E7D32)
                    )
                }
            }

            // Show saved key masked
            if (isSaved && !isVisible && value.isEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color(0xFF2E7D32).copy(alpha = 0.08f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp)
                ) {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        maskedValue,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        color = Color(0xFF2E7D32)
                    )
                }
            }

            // Input Field
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Enter API Key") },
                placeholder = { Text("Paste your key here...") },
                visualTransformation = if (isVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                trailingIcon = {
                    IconButton(onClick = onToggleVisibility) {
                        Icon(
                            if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (isVisible) "Hide" else "Show"
                        )
                    }
                },
                isError = validationError != null,
                supportingText = {
                    validationError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = keyColor,
                    focusedLabelColor = keyColor
                )
            )

            // Action Buttons Row
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Paste Button
                OutlinedButton(
                    onClick = onPasteFromClipboard,
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, keyColor)
                ) {
                    Icon(
                        Icons.Default.ContentPaste,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = keyColor
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Paste", color = keyColor, fontSize = 13.sp)
                }

                // Save Button
                Button(
                    onClick = onSave,
                    modifier = Modifier.weight(1f),
                    enabled = value.isNotEmpty() && !isValidating,
                    colors = ButtonDefaults.buttonColors(containerColor = keyColor)
                ) {
                    if (isValidating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            Icons.Default.Save,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(Modifier.width(4.dp))
                    Text("Save", fontSize = 13.sp)
                }

                // Clear Button
                if (isSaved || value.isNotEmpty()) {
                    OutlinedButton(
                        onClick = onClear,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Red
                        ),
                        border = BorderStroke(1.dp, Color.Red)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TestResultCard(result: String) {
    val isSuccess = result.contains("✅") || result.contains("Success", ignoreCase = true)
    val color = if (isSuccess) Color(0xFF2E7D32) else Color.Red

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        border = BorderStroke(1.dp, color)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                if (isSuccess) Icons.Default.CheckCircle else Icons.Default.Error,
                contentDescription = null,
                tint = color
            )
            Spacer(Modifier.width(8.dp))
            Text(result, color = color)
        }
    }
}

@Composable
fun SecurityInfoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Gray.copy(alpha = 0.08f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Security,
                    contentDescription = null,
                    tint = Color.Gray
                )
                Spacer(Modifier.width(8.dp))
                Text("Security Info", fontWeight = FontWeight.Bold, color = Color.Gray)
            }

            Spacer(Modifier.height(8.dp))

            val points = listOf(
                "🔒 Keys stored using AES-256-GCM encryption",
                "📱 Keys never leave your device",
                "🚫 Keys are not uploaded to any server",
                "🔑 Protected by Android Keystore system",
                "👁️ Keys are masked after saving"
            )

            points.forEach { point ->
                Text(
                    point,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun DangerZoneCard(onClearAll: () -> Unit) {
    var showConfirmDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Red.copy(alpha = 0.05f)
        ),
        border = BorderStroke(1.dp, Color.Red.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "⚠️ Danger Zone",
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "This will permanently delete all saved API keys.",
                fontSize = 13.sp,
                color = Color.Gray
            )
            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = { showConfirmDialog = true },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                border = BorderStroke(1.dp, Color.Red)
            ) {
                Icon(Icons.Default.DeleteForever, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Clear All API Keys")
            }
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = Color.Red) },
            title = { Text("Delete All Keys?") },
            text = { Text("All saved API keys will be permanently deleted. You will need to re-enter them.") },
            confirmButton = {
                Button(
                    onClick = {
                        onClearAll()
                        showConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Yes, Delete All")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
