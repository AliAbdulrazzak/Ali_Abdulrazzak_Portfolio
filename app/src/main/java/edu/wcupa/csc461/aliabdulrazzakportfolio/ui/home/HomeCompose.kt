package edu.wcupa.csc461.aliabdulrazzakportfolio.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.wcupa.csc461.birthdaycard.ui.theme.BirthdayCardTheme

// ── Theme definitions ────────────────────────────────────────────────────────

data class CardTheme(
    val name: String,
    val gradient: List<Color>,
    val textColor: Color,
    val accentColor: Color,
    val emoji: String
)

val cardThemes = listOf(
    CardTheme("Party",   listOf(Color(0xFFFF6B9D), Color(0xFFFFB347)), Color.White,       Color(0xFFFFE66D), "🎉"),
    CardTheme("Elegant", listOf(Color(0xFF2C3E50), Color(0xFF4CA1AF)), Color.White,       Color(0xFFD4AF37), "✨"),
    CardTheme("Ocean",   listOf(Color(0xFF1CB5E0), Color(0xFF000851)), Color.White,       Color(0xFF00FFD1), "🌊"),
    CardTheme("Sunset",  listOf(Color(0xFFF7971E), Color(0xFFFF4E50)), Color.White,       Color(0xFFFFF176), "🌅"),
    CardTheme("Garden",  listOf(Color(0xFF56AB2F), Color(0xFFA8E063)), Color(0xFF1B5E20), Color(0xFFF9A825), "🌸"),
)

// ── Entry point ──────────────────────────────────────────────────────────────




// ── App state + navigation ───────────────────────────────────────────────────

@Composable
fun BirthdayCardApp() {
    var recipientName by remember { mutableStateOf("Jordan") }
    var senderName    by remember { mutableStateOf("Ali") }
    var message       by remember { mutableStateOf("Wishing you all the joy your heart can hold!") }
    var themeIndex    by remember { mutableIntStateOf(0) }
    var isEditing     by remember { mutableStateOf(false) }

    AnimatedContent(
        targetState = isEditing,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "screen_transition"
    ) { editing ->
        if (editing) {
            EditScreen(
                recipientName     = recipientName,
                senderName        = senderName,
                message           = message,
                selectedThemeIdx  = themeIndex,
                onRecipientChange = { recipientName = it },
                onSenderChange    = { senderName = it },
                onMessageChange   = { message = it },
                onThemeChange     = { themeIndex = it },
                onDone            = { isEditing = false }
            )
        } else {
            CardScreen(
                recipientName = recipientName,
                senderName    = senderName,
                message       = message,
                theme         = cardThemes[themeIndex],
                onEdit        = { isEditing = true }
            )
        }
    }
}

// ── Card view ────────────────────────────────────────────────────────────────

@Composable
fun CardScreen(
    recipientName: String,
    senderName: String,
    message: String,
    theme: CardTheme,
    onEdit: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(theme.gradient))
    ) {
        Box(
            modifier = Modifier
                .size(220.dp)
                .offset(x = (-70).dp, y = (-70).dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.07f))
        )
        Box(
            modifier = Modifier
                .size(160.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 60.dp, y = 60.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.07f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = theme.emoji, fontSize = 64.sp)

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(20.dp, RoundedCornerShape(28.dp)),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.18f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 36.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "H A P P Y  B I R T H D A Y",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = theme.textColor.copy(alpha = 0.75f),
                        letterSpacing = 3.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "$recipientName!",
                        fontSize = 44.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = theme.accentColor,
                        textAlign = TextAlign.Center,
                        lineHeight = 52.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalDivider(
                        modifier = Modifier.width(56.dp),
                        thickness = 2.dp,
                        color = theme.textColor.copy(alpha = 0.35f)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "\u201C$message\u201D",
                        fontSize = 15.sp,
                        fontStyle = FontStyle.Italic,
                        color = theme.textColor.copy(alpha = 0.88f),
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    Text(
                        text = "— $senderName",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontStyle = FontStyle.Italic,
                        color = theme.textColor.copy(alpha = 0.75f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            OutlinedButton(
                onClick = onEdit,
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.5.dp, theme.textColor.copy(alpha = 0.6f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = theme.textColor)
            ) {
                Text(
                    text = "Customize Card",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                )
            }
        }
    }
}

// ── Edit / customize view ────────────────────────────────────────────────────

@Composable
fun EditScreen(
    recipientName: String,
    senderName: String,
    message: String,
    selectedThemeIdx: Int,
    onRecipientChange: (String) -> Unit,
    onSenderChange: (String) -> Unit,
    onMessageChange: (String) -> Unit,
    onThemeChange: (Int) -> Unit,
    onDone: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 28.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
            Text(
                text = "Customize Your Card",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = recipientName,
                onValueChange = onRecipientChange,
                label = { Text("Recipient Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = senderName,
                onValueChange = onSenderChange,
                label = { Text("Your Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = message,
                onValueChange = onMessageChange,
                label = { Text("Personal Message") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
                shape = RoundedCornerShape(12.dp)
            )

            Text(
                text = "Choose a Theme",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                cardThemes.forEachIndexed { index, theme ->
                    ThemeChip(
                        theme      = theme,
                        isSelected = index == selectedThemeIdx,
                        onClick    = { onThemeChange(index) },
                        modifier   = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = onDone,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Preview Card", fontSize = 16.sp)
            }
    }
}

// ── Theme selector chip ──────────────────────────────────────────────────────

@Composable
fun ThemeChip(
    theme: CardTheme,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary
                      else Color.Gray.copy(alpha = 0.3f)
    val borderWidth = if (isSelected) 2.5.dp else 1.dp

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(borderWidth, borderColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(theme.gradient))
        )
        Text(text = theme.emoji, fontSize = 14.sp)
        Text(
            text = theme.name,
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

// ── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CardScreenPreview() {
    BirthdayCardTheme {
        CardScreen(
            recipientName = "Jordan",
            senderName    = "Ali",
            message       = "Wishing you all the joy your heart can hold!",
            theme         = cardThemes[0],
            onEdit        = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditScreenPreview() {
    BirthdayCardTheme {
        EditScreen(
            recipientName     = "Jordan",
            senderName        = "Ali",
            message           = "Wishing you all the joy your heart can hold!",
            selectedThemeIdx  = 0,
            onRecipientChange = {},
            onSenderChange    = {},
            onMessageChange   = {},
            onThemeChange     = {},
            onDone            = {}
        )
    }
}
