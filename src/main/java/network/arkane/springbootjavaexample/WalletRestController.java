package network.arkane.springbootjavaexample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WalletRestController {

    @Autowired
    private OAuth2RestOperations oAuth2RestOperations;

    @GetMapping("/wallets")
    public String wallets() {
        return oAuth2RestOperations.getForObject("https://api-staging.arkane.network/api/wallets", String.class);
    }
}
