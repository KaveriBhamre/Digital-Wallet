package com.main;

import com.model.*;
import java.time.LocalDateTime;
import com.service.*;


import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        IWalletService service = new WalletService();

        while (true) {

        	System.out.println("\n===== DIGITAL WALLET SYSTEM =====");

        	/* ─── WALLET MANAGEMENT ─── */
        	System.out.println("---- WALLET ----");
        	System.out.println("1. Create Wallet");
        	System.out.println("2. Add Money");
        	System.out.println("3. Send Money");
        	System.out.println("4. View Balance");

        	/* ─── TRANSACTIONS ─── */
        	System.out.println("---- TRANSACTIONS ----");
        	System.out.println("5. View Transactions");
        	System.out.println("6. Show Large Transactions");
        	System.out.println("7. Sort Transactions by Amount");
        	System.out.println("8. Search Transactions by Type");
        	System.out.println("9. Search Transactions by Date Range");

        	/* ─── GROUP MANAGEMENT ─── */
        	System.out.println("---- GROUP ----");
        	System.out.println("10. Create Group");
        	System.out.println("11. Add Member to Group");
        	System.out.println("12. View My Groups");

        	/* ─── EXPENSE MANAGEMENT ─── */
        	System.out.println("---- EXPENSE ----");
        	System.out.println("13. Add Group Expense");
        	System.out.println("14. Settle Expense");
        	System.out.println("15. View Pending Settlements");
        	System.out.println("16. View Group Summary");

        	/* ─── EXIT ─── */
        	System.out.println("17. Exit");

            System.out.print("Enter choice: ");

            int choice;

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }

            try {

                switch (choice) {

                    case 1:
                        System.out.print("Enter User ID: ");
                        String userId = sc.nextLine();

                        System.out.print("Enter Name: ");
                        String name = sc.nextLine();

                        System.out.print("Enter Phone Number: ");
                        String phone = sc.nextLine();

                        System.out.print("Enter Wallet ID: ");
                        String walletId = sc.nextLine();

                        User user = new User(userId, name, phone);
                        service.createWallet(user, walletId);
                        break;

                    case 2:
                        System.out.print("Enter Wallet ID: ");
                        String wId1 = sc.nextLine();

                        System.out.print("Enter Amount: ");
                        double amount1;
                        try {
                            amount1 = Double.parseDouble(sc.nextLine());
                        } catch (Exception e) {
                            System.out.println("Invalid amount!");
                            break;
                        }

                        service.addMoney(wId1, amount1);
                        break;

                    case 3:
                        System.out.print("Enter Sender Wallet ID: ");
                        String fromId = sc.nextLine();

                        System.out.print("Enter Receiver Wallet ID: ");
                        String toId = sc.nextLine();

                        System.out.print("Enter Amount: ");
                        double amount2;
                        try {
                            amount2 = Double.parseDouble(sc.nextLine());
                        } catch (Exception e) {
                            System.out.println("Invalid amount!");
                            break;
                        }

                        service.sendMoney(fromId, toId, amount2);
                        break;

                    case 4:
                        
                        System.out.print("Enter Wallet ID: ");
                        String wId6 = sc.nextLine();
                        service.viewBalance(wId6);
                        break;
                        

                    case 5:
                    	System.out.print("Enter Wallet ID: ");
                        String wId2 = sc.nextLine();

                        service.viewTransactions(wId2);
                        break;
                    	
                   

                    case 6:
                    	System.out.print("Enter Wallet ID: ");
                        String wId3 = sc.nextLine();

                        System.out.print("Enter Limit Amount: ");
                        double limit;
                        try {
                            limit = Double.parseDouble(sc.nextLine());
                        } catch (Exception e) {
                            System.out.println("Invalid amount!");
                            break;
                        }

                        service.showLargeTransactions(wId3, limit);
                        break;
                    	
                        
                    case 7:
                    	System.out.print("Enter Wallet ID: ");
                        String wId4 = sc.nextLine();

                        service.sortTransactionsByAmount(wId4);
                        break;

                        

                    case 8:
                    	System.out.print("Enter Wallet ID: ");
                        String wId8 = sc.nextLine();
                        System.out.print("Enter Type (CREDIT/DEBIT): ");
                        String typeStr = sc.nextLine().trim().toUpperCase();
                        TransactionType txType;
                        try {
                            txType = TransactionType.valueOf(typeStr);
                        } catch (IllegalArgumentException ex) {
                            System.out.println("Invalid type. Use CREDIT or DEBIT.");
                            break;
                        }
                        service.searchByType(wId8, txType);
                        break;

                    case 9:
                    	
                    	System.out.print("Enter Wallet ID: ");
                        String wId9 = sc.nextLine();
                        System.out.print("Enter From date-time (yyyy-MM-ddTHH:mm): ");
                        LocalDateTime from;
                        LocalDateTime to;
                        try {
                            from = LocalDateTime.parse(sc.nextLine().trim());
                            System.out.print("Enter To date-time (yyyy-MM-ddTHH:mm): ");
                            to = LocalDateTime.parse(sc.nextLine().trim());
                        } catch (Exception ex) {
                            System.out.println("Invalid date format. Use yyyy-MM-ddTHH:mm (e.g. 2025-01-01T00:00).");
                            break;
                        }
                        service.searchByDateRange(wId9, from, to);
                        break;
                    	
                        

                    case 10:
                    	System.out.print("Enter Group ID: ");
                        String gId = sc.nextLine();
                        System.out.print("Enter Group Name: ");
                        String gName = sc.nextLine();
                        System.out.print("Enter Creator Wallet ID: ");
                        String creatorId = sc.nextLine();
                        service.createGroup(gId, gName, creatorId);
                        break;
                    	
                    

                    case 11:
                    	System.out.print("Enter Group ID: "); 
                    	String gId2 = sc.nextLine(); 
                    	System.out.print("Enter Member Wallet ID to Add: "); 
                    	String memberId = sc.nextLine(); 
                    	service.addMemberToGroup(gId2, memberId); 
                    	break;
                    	
                        

                    case 12:                  
                    	System.out.print("Enter Wallet ID: ");
                        String wId7 = sc.nextLine();
                        service.viewUserGroups(wId7);
                        break;
                    	
                       
                        
                    case 13:
                    	
                    	System.out.print("Enter Group ID: ");
                        String gId3 = sc.nextLine();
                        System.out.print("Enter Payer Wallet ID: ");
                        String payerId = sc.nextLine();
                        System.out.print("Enter Description (e.g. Dinner): ");
                        String desc = sc.nextLine();
                        System.out.print("Enter Total Amount: ");
                        double expAmt;
                        try {
                            expAmt = Double.parseDouble(sc.nextLine());
                        } catch (Exception e) {
                            System.out.println("Invalid amount!");
                            break;
                        }
                        service.addGroupExpense(gId3, payerId, expAmt, desc);
                        break;
                    	
                       
                        
                    case 14:
                    	System.out.print("Enter Expense ID: ");
                        String expId = sc.nextLine();
                        System.out.print("Enter Your Wallet ID: ");
                        String debtorId = sc.nextLine();
                        service.settleExpense(expId, debtorId);
                        break;
                    	
                    

                    case 15:
                    	System.out.print("Enter Wallet ID: ");
                        String wId5 = sc.nextLine();
                        service.viewPendingSettlements(wId5);
                        break;
                        

                    case 16:
                    	System.out.print("Enter Group ID: ");
                        String gId4 = sc.nextLine();
                        service.viewGroupSummary(gId4);
                        break;

                    case 17:
                        System.out.println("Exiting... Thank you!");
                        sc.close();
                        return;


                    default:
                        System.out.println("Invalid choice!");
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}