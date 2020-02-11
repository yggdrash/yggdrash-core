package io.yggdrash.core.blockchain;

import com.google.gson.JsonObject;
import io.yggdrash.common.contract.ContractVersion;
import io.yggdrash.common.crypto.ECKey;
import io.yggdrash.common.util.VerifierUtils;
import io.yggdrash.core.wallet.Wallet;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.util.encoders.Hex;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.*;

public class TransactionBuilderTest {

    ECKey key = ECKey.fromPrivate(Hex.decode("c85ef7d79691fe79573b1a7064c19c1a9819ebdbd1faaab1a8ec92344438aaf4"));
    Wallet w = new Wallet(key);
    private static final Logger log = LoggerFactory.getLogger(TransactionBuilderTest.class);

    @Test
    public void transactionBuilderTest() {
        JsonObject body = new JsonObject();
        body.addProperty("tokenId", "TTOKEN".toLowerCase());
        body.addProperty("tokenName", "TTOKEN");
        body.addProperty("tokenInitYeedStakeAmount", BigInteger.TEN.pow(50));
        body.addProperty("tokenInitMintAmount", BigInteger.TEN.pow(30));
        body.addProperty("tokenMintable", true);
        body.addProperty("tokenBurnable", true);
        body.addProperty("tokenExT2YEnabled", false);
        body.addProperty("tokenExT2YType", "TOKEN_EX_T2Y_TYPE_FIXED");
        body.addProperty("tokenExT2YRate", new BigDecimal("1.0"));

        ContractVersion version = ContractVersion.of("f6e516c5e76cc2014fd6ab0cf8b7f125837c4f8a");

        Transaction createTx = new TransactionBuilder()
                .setTxBody(version, "createToken", body)
                .setBranchId(BranchId.of("da2d5b7370d04695a2ff81f9f0ab0572ad6d222d"))
                .setWallet(w)
                .build();

        log.debug(createTx.toJsonObject().toString());
        assertTrue(VerifierUtils.verifySignature(createTx));
        assertEquals(0, VerifierUtils.verifyDataFormatCode(createTx));

        JsonObject creatTxObject = createTx.toJsonObject();
        JsonObject txHeader = creatTxObject.getAsJsonObject("header");
        assertEquals(txHeader.get("chain").getAsString() ,"da2d5b7370d04695a2ff81f9f0ab0572ad6d222d");


    }

}