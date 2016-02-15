package api

import models.LoanTableRows.LoanTableRows

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer


object Api {
  /**
   * Calculates the rate of interest assessed on a loan, over a set time period when compounding occurs more than once per year.
   *
   * @param annualInterest  Annual interest of loan
   * @param paymentsPerYear  Number of payments per year of loan
   * @return  Periodic Interest Rate
   */
  def periodicInterestRate(annualInterest: Double, paymentsPerYear: Int): Double = {
    (annualInterest / 100) / paymentsPerYear
  }

  /**
   * Calculates monthly principal of loan
   *
   * @param P  Principal amount borrowed
   * @param n  Number of all payments
   * @param r  Periodic interest rate
   * @param grace  Number of months without principal payment
   * @param month  Number of month payment
   * @return  The sum you need to pay at the end of each month (Periodic Amortization Payment)
   */
  def periodicAmortizationPayment(P: BigDecimal, n: Int, r: Double, grace: Int, month: Int = 0): BigDecimal = {
    if(grace > 0 && month < grace)
      P * r
    else
      P * r * Math.pow( 1 + r, n - grace ) / ( Math.pow( 1 + r, n - grace ) - 1 )
  }

  /**
   * Calculates monthly principal of loan
   *
   * @param A  Periodic Amortization Payment
   * @param interest  Month interest
   * @param grace  Number of months without principal payment
   * @param month  Number of month payment
   * @return  Amount of month principal
   */
  def principal(A: BigDecimal, interest: BigDecimal, grace: Int, month: Int = 0): BigDecimal = {
    if(grace > 0 && month < grace) 0 else A - interest
  }

  /**
   * Calculates Equated Monthly Installments (EMI) of loan
   *
   * @param P  Principal amount borrowed
   * @param annualInterest  Annual incest of loan
   * @param loanPeriodYears  Period of the loan in yeas
   * @param paymentsPerYear  Monthly payments per year
   * @return  List of Equated monthly installments
   */
  def calculateEquatedMonthlyInstallments(P: BigDecimal, annualInterest: Double, paymentsPerYear: Int, loanPeriodYears: Int, gracePeriod: Int, promotionalPeriod: Int, promotionalInterest: Double): List[LoanTableRows] = {
    //Number of all payments
    val n: Int = loanPeriodYears * paymentsPerYear
    //Periodic interest rate
    val r: Double = periodicInterestRate(annualInterest, paymentsPerYear)
    //periodic amortization payment
    val A: BigDecimal = periodicAmortizationPayment(P, n, r, gracePeriod)

    @tailrec
    def calculate(acc: List[LoanTableRows], paymentsLeft: BigDecimal, monthInterest: BigDecimal, monthPrincipal: BigDecimal, monthPayment: BigDecimal, totalAmount: BigDecimal): List[LoanTableRows] = {
      val month = acc.length + 1

      if (month >= n) {
        //fill the accumulated total amount and total interest in all the model fields
        acc.foreach{el => {el.totalAmount = totalAmount; el.totalInterest = totalAmount - P}}
        acc :+ LoanTableRows(month, paymentsLeft, monthPrincipal, monthInterest, monthPayment, paymentsPerYear, loanPeriodYears, totalAmount, totalAmount - P)
      } else {
        val entry = LoanTableRows(month, paymentsLeft, monthPrincipal, monthInterest, monthPayment, paymentsPerYear, loanPeriodYears)
        val nextPayment: BigDecimal = periodicAmortizationPayment(P, n, r, gracePeriod, month)
        val nextPaymentsLeft: BigDecimal = paymentsLeft - monthPrincipal
        val nextInterest: BigDecimal = nextPaymentsLeft * r
        val nextPrincipal: BigDecimal = principal(nextPayment, nextInterest, gracePeriod, month)
        val nextTotalAmount = totalAmount + nextPayment
        calculate(acc:+entry, nextPaymentsLeft, nextInterest, nextPrincipal, nextPayment, nextTotalAmount)
      }
    }

    val monthInterest: BigDecimal = P * r
    val monthPrincipal: BigDecimal = principal(A, monthInterest, gracePeriod)
    calculate(Nil, P, monthInterest, monthPrincipal, A, monthInterest + monthPrincipal)
  }

  /**
   * Calculates Equated Monthly Installment (EPP) of loan
   *
   * @param P               = Principal amount borrowed
   * @param annualInterest  = Annual incest of loan
   * @param loanPeriodYears = Period of the loan in yeas
   * @param paymentsPerYear = Monthly payments per year
   * @return EPPData        = List of tuples
   */
  def calculateEqualPrincipalPayments(P: BigDecimal, annualInterest: Double, paymentsPerYear: Int, loanPeriodYears: Int, gracePeriod: Int, promotionalPeriod: Int, promotionalInterest: Double) = {
    //Total Number of Payments
    val n = loanPeriodYears * paymentsPerYear

    //Periodic Interest Rate
    val r = (annualInterest / 100) / paymentsPerYear

    val promotionalR = (promotionalInterest / 100) / paymentsPerYear

    var monthPrincipal: BigDecimal = P / n

    var EPPData = new ListBuffer[LoanTableRows]

    var totalAmount: BigDecimal = 0

    for(i <- 0 to n-1) {
      val totalPrincipalLeft: BigDecimal = if(gracePeriod > 0 && (gracePeriod - 1) >= i ) P else P - ( (i - gracePeriod) * monthPrincipal)

      monthPrincipal = if(gracePeriod > 0 && (gracePeriod - 1) >= i ) 0 else P / (n - gracePeriod)

      val monthInterest: BigDecimal = if(promotionalPeriod > 0 && (promotionalPeriod - 1) >= i) totalPrincipalLeft * promotionalR else totalPrincipalLeft * r

      //calculate Periodic Amortization Payment
      val A = if(gracePeriod > 0 && (gracePeriod - 1) >= i ) monthInterest else monthPrincipal + monthInterest

      totalAmount += A

      EPPData += LoanTableRows(
        i + 1,                                  //month
        totalPrincipalLeft - monthPrincipal,  //startBalance
        monthPrincipal,                         //principal
        monthInterest,                          //interest
        A,                                      //payment
        paymentsPerYear,                        //paymentsPerYear
        loanPeriodYears                         //years
      )
    }

    val totalInterest: BigDecimal = totalAmount - P


    for(i <- EPPData.indices) {
      EPPData(i).totalAmount = totalAmount
      EPPData(i).totalInterest = totalInterest
    }

    EPPData.toList
  }
}
