package api.contracts.dto;

public class CottageDto {
    public int Id;
    public String Title;
    public int Beds;
    public String Image;

    public CottageDto(int id, String title, int bedcount, String imageurl) {
        Id = id;
        Title = title;
        Beds = bedcount;
        Image = imageurl;
    }
}
