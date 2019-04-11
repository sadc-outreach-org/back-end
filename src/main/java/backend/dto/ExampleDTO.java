package backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExampleDTO
{
    private int exampleID;
    private int ccID;
    private String input;
    private String output;
    private String explanation;
}