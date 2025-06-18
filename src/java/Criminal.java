public class Criminal {
    private String id;
    private String name;
    private String crime;
    private String address;
    private String city;
    private String year;
    private String mobile;
    
    // Constructors
    public Criminal() {}
    
    public Criminal(String id, String name, String crime, String address) {
        this.id = id;
        this.name = name;
        this.crime = crime;
        this.address = address;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCrime() {
        return crime;
    }
    
    public void setCrime(String crime) {
        this.crime = crime;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getYear() {
        return year;
    }
    
    public void setYear(String year) {
        this.year = year;
    }
    
    public String getMobile() {
        return mobile;
    }
    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    
    @Override
    public String toString() {
        return "ID: " + id + "\n" +
               "Name: " + name + "\n" +
               "Crime: " + crime + "\n" +
               "Address: " + address + "\n" +
               (city != null ? "City: " + city + "\n" : "") +
               (year != null ? "Year: " + year + "\n" : "") +
               (mobile != null ? "Mobile: " + mobile : "");
    }
}
