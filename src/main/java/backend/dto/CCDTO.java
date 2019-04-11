package backend.dto;

import java.util.Set;

import backend.model.Example;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CCDTO
{
    private int ccID;
    private String name;
    private String description;
    private Set<Example> examples;
}