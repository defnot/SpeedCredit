package controllers

import api.Api
import play.api.libs.json._
import play.api.mvc._

object AppLogic extends Controller {
  def loadEMIData(loan: Double, annualInterest: Double, paymentsPerYear: Int, loanPeriodYears: Int, gracePeriod: Int, promotionalPeriod: Int, promotionalInterest: Double) = Action {

    val loanTableRows = Api.calculateEquatedMonthlyInstallments(loan, annualInterest, paymentsPerYear, loanPeriodYears, gracePeriod, promotionalPeriod, promotionalInterest)
    Ok(Json.toJson(loanTableRows))
  }

  def loadEPPData(loan: Double, annualInterest: Double, paymentsPerYear: Int, loanPeriodYears: Int, gracePeriod: Int, promotionalPeriod: Int, promotionalInterest: Double) = Action {

    val loanTableRows = Api.calculateEqualPrincipalPayments(loan, annualInterest, paymentsPerYear, loanPeriodYears, gracePeriod, promotionalPeriod, promotionalInterest)
    Ok(Json.toJson(loanTableRows))
  }
}