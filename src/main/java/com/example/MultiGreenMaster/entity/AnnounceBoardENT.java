package com.example.MultiGreenMaster.entity;
// ============================================= 공지사항 entity =====================================


import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Table(name = "announceboard")
@Getter
@ToString
@Entity
@Builder
public class AnnounceBoardENT {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) //이 코드 때문에 더미 파일에 id값을 빼었다. 에러가 나기 때문.
    private Long id;
    @Column
    private String title;
    @Column
    private String content;
    @Column
    private boolean disable;

    public void patch(AnnounceBoardENT announce){ //update를 원하지 않는 필드들이 null로 변하지 않게(유지되게) 하는 목적.
        if(announce.title != null){
            this.title = announce.title;
        }
        if(announce.content != null){
            this.content = announce.content;
        }
    }

    public void toDisable() {
        this.disable = true;
    }


}
