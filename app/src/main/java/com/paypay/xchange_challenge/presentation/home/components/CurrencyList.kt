/**
 * Project Name: Currency Xchange
 * Created by: MUTABAZI Charles on 12/02/2023
 */
package com.paypay.xchange_challenge.presentation.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.paypay.xchange_challenge.presentation.home.HomeViewModel

@Composable
fun CurrencyList(vm: HomeViewModel) {
    // LazyColumn of currencies
    Column {
        LazyColumn {
            if (vm.convertedAmountList.isEmpty()) {

                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Please enter amount and click Get Rates",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }
                return@LazyColumn
            }

            items(items = vm.convertedAmountList) { currency ->
                // Row of currency
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column {
                        Text(
                            text = currency.symbol,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = currency.name,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "%.2f".format(currency.rate), //<-- formatted to 2 decimal places
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (currency != vm.convertedAmountList.last()) {
                    Divider()
                }
            }
        }
    }
}