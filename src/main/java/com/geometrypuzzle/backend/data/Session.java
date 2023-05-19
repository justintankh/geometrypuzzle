package com.geometrypuzzle.backend.data;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.geometrypuzzle.backend.shape.Shape;
import com.geometrypuzzle.backend.utils.Utils;
import com.geometrypuzzle.backend.workflow.Step;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Session {
    @Id
    @SequenceGenerator(
            name = "workflow_id_sequence",
            sequenceName = "workflow_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.UUID,
            generator = "workflow_id_sequence"
    )
    private String processKey;
    private String businessKey;
    private Step step;
    private String shapeAsJson;
    public Session(){}
    public Shape getShape() throws JsonProcessingException {
            return Utils.readValue(this.shapeAsJson, Shape.class);
    }
}
