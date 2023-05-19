package com.geometrypuzzle.backend.data;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.geometrypuzzle.backend.shape.Shape;
import com.geometrypuzzle.backend.utils.Utils;
import com.geometrypuzzle.backend.workflow.Step;
import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
public class Session {
    @Id
    private String processKey;

    private Step step;

    private String shapeAsJson;
    public Session(){}
    public Shape getShape() throws JsonProcessingException {
            return Utils.readValue(this.shapeAsJson, Shape.class);
    }
}
