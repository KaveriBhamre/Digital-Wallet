package com.model;

public final class User {
	private final String userId;
	private final String name;
	private final String phoneNumber;
	
	public User(String userId, String name, String phoneNumber) {
		if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        if (phoneNumber == null || !phoneNumber.matches("\\d{10}")) {
            throw new IllegalArgumentException("Phone number must be 10 digits");
        }
		
		this.userId = userId;
		this.name = name;
		this.phoneNumber = phoneNumber;
	}
	
	public String getUserId() {
		return this.userId;
	}

	public String getName() {
		return name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	@Override
	public String toString() {
		return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
	}

	@Override
	public int hashCode() {
		int result = 17;   
        result = 31 * result + (userId == null ? 0 : userId.hashCode());
        return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        User other = (User) obj;

        if (userId == null) {
            return other.userId == null;
        }
        return userId.equals(other.userId);
	}

}
