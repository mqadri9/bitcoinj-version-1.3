package org.bitcoinj.examples;
import static org.bitcoinj.core.Coin.COIN;

import java.io.File;
import java.util.Set;

import org.bitcoinj.core.*;
import org.bitcoinj.core.ECKey.ECDSASignature;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.utils.BriefLogFormatter;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.wallet.SendRequest;


public class reqtx {
	public static byte[] hexStringToByteArray(String s) {
		  int len = s.length();
		  byte[] data = new byte[len / 2];
		  for (int i = 0; i < len; i += 2) {
			  data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
					  				+ Character.digit(s.charAt(i+1), 16));
		  }
		  return data;
	  }
	public static void main(String[] args) throws Exception {
        BriefLogFormatter.init();
        final RegTestParams params = RegTestParams.get();
        byte[] checksum1 = hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d");
        byte[] checksum2 = hexStringToByteArray("e04fd020ea3a6910a2d808002b30309e");
        boolean flag=true;
        int OP_EQUAL=0x87;  
        int OP_DUP=0x76;  
        int OP_HASH160=0xa9;  
        int OP_EQUALVERIFY=0x88;  
        int OP_CHECKSIG=0xac;  
        int OP_VERIFY=0x69;

        WalletAppKit wallet1 = new WalletAppKit(params, new File("."), "wallet1");
        WalletAppKit wallet2 = new WalletAppKit(params, new File("."), "wallet2");
        WalletAppKit wallet3 = new WalletAppKit(params, new File("."), "wallet3");
        WalletAppKit wallet4 = new WalletAppKit(params, new File("."), "wallet4");
	    
        
        wallet1.connectToLocalHost();
        wallet1.setAutoSave(false);
        wallet1.startAsync();
        wallet1.awaitRunning();
        wallet2.connectToLocalHost();
        wallet2.setAutoSave(false);
        wallet2.startAsync();
        wallet2.awaitRunning();
        wallet3.connectToLocalHost();
        wallet3.setAutoSave(false);
        wallet3.startAsync();
        wallet3.awaitRunning();
        wallet4.connectToLocalHost();
        wallet4.setAutoSave(false);
        wallet4.startAsync();
        wallet4.awaitRunning();
        Transaction temp;
        Address address4 = wallet4.wallet().currentReceiveAddress();
        Address address3 = wallet3.wallet().currentReceiveAddress();
        Set<Transaction> transactions = wallet3.wallet().getTransactions(true);
        temp = (Transaction) transactions.toArray()[0];
        TransactionOutput output = temp.getOutput(1);
        //Transaction temp = new Transaction(params, hexStringToByteArray("010000000194c702d27e58d735a02f923d8c5aa1ff71268cddee7070c0eca1cb359e938d9e000000006b48304502210092d503ebaf0a9a8d261dfe13a072e5e6d976d10f1900961dc4c6a3427b969104022076939eb4628cac028e70f9404fabda87c13163e92eb0be62f7066d2eb362930801210312024e44dd6fb38949e8aaa8e5bd39a6a3080ef9dfffd202f383b37019a7fad2ffffffff02f4ff191e010000001976a914ce696eb0be0a6f043e3671b7bf03be908144f63288ac00c2eb0b000000002c76a91421079aca53eb99b582d4277d9848c176ae9e980588ac6910e04fd020ea3a6910a2d808002b30309d8700000000") );
        //TransactionOutput output = temp.getOutput(1);	        
        //System.out.println("Tansaction output:\n"+output);	 
        
        SendRequest req = SendRequest.to(address4, COIN.multiply(1));
        req.tx.addInput(output);
        wallet3.wallet().completeTx(req);
        /*wallet3.wallet().signTransaction(req);
        req.tx.getConfidence().setSource(TransactionConfidence.Source.SELF);
        req.tx.setPurpose(Transaction.Purpose.USER_PAYMENT);
        req.tx.setExchangeRate(req.exchangeRate);
        req.tx.setMemo(req.memo);
        req.completed = true; */
        System.out.println(req.tx);
        final Peer peer = wallet3.peerGroup().getConnectedPeers().get(0);
        peer.sendMessage(req.tx);
        Thread.sleep(1000);
        wallet1.stopAsync();
        wallet1.awaitTerminated();
        wallet2.stopAsync();
        wallet2.awaitTerminated();
        wallet3.stopAsync();
        wallet3.awaitTerminated();
        wallet4.stopAsync();
        wallet4.awaitTerminated();
   
	}	
}
