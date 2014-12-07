package com.wadpam.guja.api;


import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;

import javax.annotation.security.PermitAll;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Set;

import static com.google.common.base.Preconditions.*;

/**
 * Check if an apps version is supported or not.
 * @author mattiaslevin
 */
@Path("api/version")
@Singleton
@PermitAll
public class VersionCheckResource {

    private final Set<VersionCheckPredicate> predicates;


    @Inject
    public VersionCheckResource(Set<VersionCheckPredicate> predicates) {
        this.predicates = checkNotNull(predicates);
    }


    @GET
    @Path("{version}/check")
    public Response checkVersion(@QueryParam("platform") String platform,
                                 @PathParam("version") String version) {
        checkNotNull(platform);
        checkNotNull(version);

        boolean isVersionAllowed = predicates.isEmpty();
        for (VersionCheckPredicate predicate : predicates) {
            Optional<Boolean> optional = predicate.isVersionSupported(platform, version);
            if (optional.isPresent()) {
                isVersionAllowed = optional.get();
                break;
            }
        }

        if (isVersionAllowed) {
            return Response.ok().build();
        } else {
            // TODO Localisation
            return Response.status(Response.Status.GONE)
                    .entity(ImmutableMap.builder()
                    .put("localizedMessage", "<Change me>")
                    .put("url", "<http://www.change.me>"))
                    .build();
        }

    }


    /**
     * Interface for testing if a version is supported.
     */
    public static interface VersionCheckPredicate {
        /**
         *
         * @param platform App platform
         * @param version App version
         * @return If the predicate can not decide if the version is supported of not return an optional with absent value.
         *         Otherwise return an optional with value true or false.
         */
        Optional<Boolean> isVersionSupported(String platform, String version);
    }


    /** A version check function that always will return true */
    public static class AlwaysReturnTruePredicate extends AlwaysReturnSameValuePredicate {
        public AlwaysReturnTruePredicate() {
            super(true);
        }
    }

    /** A version check function that always will return false */
    public static class AlwaysReturnFalsePredicate extends AlwaysReturnSameValuePredicate {
        public AlwaysReturnFalsePredicate() {
            super(false);
        }
    }

    private static class AlwaysReturnSameValuePredicate implements VersionCheckPredicate {

        private final boolean value;

        public AlwaysReturnSameValuePredicate(boolean value) {
            this.value = value;
        }

        @Override
        public Optional<Boolean> isVersionSupported(String platform, String version) {
            return Optional.of(value);
        }
    }

    /** if the version is included in the white list return true */
    public static class WhitelistPredicate extends ContainsPredicate {
        public WhitelistPredicate(Collection<String> list) {
            super(list, true);
        }
    }

    /** If the version is included in the back list return false */
    public static class BlacklistPredicate extends ContainsPredicate {
        public BlacklistPredicate(Collection<String> list) {
            super(list, false);
        }
    }

    private static class ContainsPredicate implements VersionCheckPredicate {

        private Collection<String> collection;
        private final boolean trueOrFalse;

        public ContainsPredicate(Collection<String> list, boolean trueOrFalse) {
            this.collection = list;
            this.trueOrFalse = trueOrFalse;
        }

        @Override
        public Optional<Boolean> isVersionSupported(String platform, String version) {
            return collection.contains(version) ? Optional.of(trueOrFalse) : Optional.<Boolean>absent();
        }

    }


}
