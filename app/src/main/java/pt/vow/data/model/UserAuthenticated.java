package pt.vow.data.model;

    // Class with the resulting object from the REST login service
// The object is automatically constructed by RETROFIT2
    public class UserAuthenticated {

        String username;
        String tokenID;
        long creationData;
        long expirationData;
        long role;

        public void UserAuthenticated(String username, String tokenID, long creationData, long expirationData, long role) {
            this.username = username;
            this.tokenID = tokenID;
            this.creationData = creationData;
            this.expirationData = expirationData;
            this.role = role;
        }

        public String getUsername() {
            return username;
        }

        public String getTokenID() {
            return tokenID;
        }

        public long getCreationData() {
            return creationData;
        }

        public long getExpirationData() {
            return expirationData;
        }

        public long getRole() { return role; }

    }
