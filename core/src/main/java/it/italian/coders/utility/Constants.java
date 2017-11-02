package it.italian.coders.utility;

import it.italian.coders.model.authentication.Authorities;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static List<String> DEFAUL_AUTHORITIES = null;
    static {
        DEFAUL_AUTHORITIES = new ArrayList<>();
        DEFAUL_AUTHORITIES.add(Authorities.ROLE_ACCESS.name());
    }

}
