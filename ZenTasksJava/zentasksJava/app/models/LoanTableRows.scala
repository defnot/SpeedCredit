package models

import play.api.libs.json.Json

object LoanTableRows {

  case class LoanTableRows(month: Int, startBalance: BigDecimal, principal: BigDecimal, interest: BigDecimal, payment: BigDecimal, paymentsPerYear: Int, years: Int, var totalAmount: BigDecimal = 0, var totalInterest: BigDecimal = 0)

  implicit val loanTableRowsWrites = Json.writes[LoanTableRows]

}