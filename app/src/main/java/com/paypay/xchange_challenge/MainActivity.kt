package com.paypay.xchange_challenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paypay.xchange_challenge.presentation.theme.CurrencyXchangeTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyXchangeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ExchangeRateScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeRateScreen() {
    var amount by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Currency Xchange",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        OutlinedTextField(
            placeholder = { Text("Enter amount") },
            value = amount,
            onValueChange = { amount = it },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            leadingIcon = {
                TextButton(onClick = { /*TODO*/ }) {
                    Text(text = "JPY", modifier = Modifier.padding(end = 8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24),
                        contentDescription = "JPY",
                        modifier = Modifier
                            .size(24.dp)
                            .clip(MaterialTheme.shapes.small)
                    )
                }
            }
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        ) {
            Button(onClick = { /*TODO*/ }) {
                Text("Get Rates")
            }
        }


        CurrencyList()
    }
}

@Composable
fun CurrencyList() {
    val currencies = mapOf(
        "USD" to "US Dollar",
        "EUR" to "Euro",
        "JPY" to "Japanese Yen",
        "GBP" to "British Pound",
        "AUD" to "Australian Dollar",
        "CAD" to "Canadian Dollar",
        "CHF" to "Swiss Franc",
        "CNY" to "Chinese Yuan",
        "HKD" to "Hong Kong Dollar",
        "NZD" to "New Zealand Dollar",
        "SEK" to "Swedish Krona",
        "SGD" to "Singapore Dollar",
        "KRW" to "South Korean Won",
        "MXN" to "Mexican Peso",
        "INR" to "Indian Rupee",
        "RUB" to "Russian Ruble",
        "ZAR" to "South African Rand",
        "TRY" to "Turkish Lira",
        "BRL" to "Brazilian Real"
    )

    // LazyColumn of currencies
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Currency",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Rate",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        Divider()
        LazyColumn {
            item {
                // Header

            }
            items(currencies.keys.size) { index ->
                val currency = currencies.keys.elementAt(index)
                val currencyName = currencies.values.elementAt(index)

                // Row of currency
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(text = "$currencyName ($currency)")
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "321.00",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (index != currencies.keys.size - 1) {
                    Divider()
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CurrencyXchangeTheme {
        ExchangeRateScreen()
    }
}