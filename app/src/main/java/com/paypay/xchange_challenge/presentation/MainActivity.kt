package com.paypay.xchange_challenge.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.paypay.xchange_challenge.R
import com.paypay.xchange_challenge.presentation.home.HomeViewModel
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
fun ExchangeRateScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Button(onClick = { viewModel.deleteCurrencyTable() }) {
                Text("Get Rates")
            }
        }


        CurrencyList(vm = viewModel)
    }
}

@Composable
fun CurrencyList(vm: HomeViewModel) {

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
            if (vm.getCurrentListing.currencies.isEmpty()) {
                items(1) {
                    Text(text = "No data")
                }
                return@LazyColumn
            }

            items(items = vm.getCurrentListing.currencies) {currency ->
                // Row of currency
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(text = "${currency.name} (${currency.symbol})")
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${currency.rate}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (currency != vm.getCurrentListing.currencies.last()) {
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