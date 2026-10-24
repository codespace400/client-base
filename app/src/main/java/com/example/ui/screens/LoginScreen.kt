package com.example.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.IndigoContainer
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate900
import com.example.ui.theme.TealAccent

@Composable
fun LoginScreen(
  onLoginSuccess: (workspace: String, email: String) -> Unit,
  modifier: Modifier = Modifier
) {
  var workspace by remember { mutableStateOf("Acme Global CRM") }
  var email by remember { mutableStateOf("alex.morgan@acmeglobal.com") }
  var password by remember { mutableStateOf("••••••••••••") }
  var rememberMe by remember { mutableStateOf(true) }
  var passwordVisible by remember { mutableStateOf(false) }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Slate50)
      .padding(16.dp),
    contentAlignment = Alignment.Center
  ) {
    Card(
      modifier = Modifier
        .widthIn(max = 460.dp)
        .fillMaxWidth()
        .border(1.dp, Slate200, RoundedCornerShape(16.dp))
        .testTag("login_card"),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(
        modifier = Modifier
          .padding(32.dp)
          .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // App Logo & Header
        Box(
          modifier = Modifier
            .size(52.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(IndigoPrimary),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Business,
            contentDescription = "ClientBase Logo",
            tint = Color.White,
            modifier = Modifier.size(28.dp)
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "Sign in to ClientBase",
          color = Slate900,
          fontSize = 22.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = (-0.4).sp
        )

        Text(
          text = "Desktop-ready lightweight CRM for high-performing teams",
          color = Slate500,
          fontSize = 13.sp,
          modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )

        // Workspace Input
        Text(
          text = "Business Workspace / Domain",
          color = Slate900,
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp)
        )
        OutlinedTextField(
          value = workspace,
          onValueChange = { workspace = it },
          placeholder = { Text("e.g. acme-corp.crm", fontSize = 14.sp) },
          leadingIcon = {
            Icon(Icons.Default.Business, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(20.dp))
          },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("login_workspace_input"),
          shape = RoundedCornerShape(8.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = IndigoPrimary,
            unfocusedBorderColor = Slate200
          )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Work Email
        Text(
          text = "Work Email",
          color = Slate900,
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp)
        )
        OutlinedTextField(
          value = email,
          onValueChange = { email = it },
          placeholder = { Text("you@company.com", fontSize = 14.sp) },
          leadingIcon = {
            Icon(Icons.Default.Email, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(20.dp))
          },
          singleLine = true,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("login_email_input"),
          shape = RoundedCornerShape(8.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = IndigoPrimary,
            unfocusedBorderColor = Slate200
          )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password
        Text(
          text = "Password",
          color = Slate900,
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp)
        )
        OutlinedTextField(
          value = password,
          onValueChange = { password = it },
          placeholder = { Text("Enter business password", fontSize = 14.sp) },
          leadingIcon = {
            Icon(Icons.Default.Lock, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(20.dp))
          },
          trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
              Icon(
                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = "Toggle password visibility",
                tint = Slate500
              )
            }
          },
          visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
          singleLine = true,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("login_password_input"),
          shape = RoundedCornerShape(8.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = IndigoPrimary,
            unfocusedBorderColor = Slate200
          )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Remember workspace checkbox
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Checkbox(
            checked = rememberMe,
            onCheckedChange = { rememberMe = it },
            colors = CheckboxDefaults.colors(checkedColor = IndigoPrimary),
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Remember this workspace on this device",
            color = Slate600,
            fontSize = 12.sp
          )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Sign In Button
        Button(
          onClick = {
            onLoginSuccess(workspace, email)
          },
          colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .testTag("login_submit_btn")
        ) {
          Text(
            text = "Sign in to ClientBase",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
          )
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(color = Slate200)
        Spacer(modifier = Modifier.height(16.dp))

        // Quick Workspace Demo Presets
        Text(
          text = "OR SIGN IN WITH A DEMO WORKSPACE",
          color = Slate500,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 0.5.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        val demoWorkspaces = listOf(
          Triple("Acme Global CRM", "alex.morgan@acmeglobal.com", "Enterprise"),
          Triple("NovaTech SaaS", "sarah@novatech.io", "Tech & Cloud"),
          Triple("Solstice Studio", "team@solsticeagency.design", "Creative Agency")
        )

        demoWorkspaces.forEach { (demoWs, demoEmail, badge) ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(IndigoContainer)
              .clickable {
                workspace = demoWs
                email = demoEmail
                onLoginSuccess(demoWs, demoEmail)
              }
              .padding(horizontal = 12.dp, vertical = 8.dp)
              .padding(bottom = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(
                text = demoWs,
                color = IndigoPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
              )
              Text(
                text = demoEmail,
                color = Slate500,
                fontSize = 11.sp
              )
            }
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(TealAccent.copy(alpha = 0.15f))
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = badge,
                color = TealAccent,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
          Spacer(modifier = Modifier.height(6.dp))
        }
      }
    }
  }
}
