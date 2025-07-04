package study.data_jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Controller;

@Entity
@Getter  @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of={"id","username","age"}) // 연관관계가 있을 시, 무한루프 방지를 위해 지정해서 toString지정
//@NamedQuery(
//        name="Member.findByUsername",
//        query="select m from Member m where m.username = :username") //실무에서 거의 안씀
//@NamedEntityGraph(name="Member.all", attributeNodes = @NamedAttributeNode("team"))
public class Member extends BaseEntity{

    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;
    private String username;
    private  int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;


    public Member(String username) {
        this(username,0);
    }

    public Member(String username, int age){
        this(username,age,null);
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if(team != null) {
            changeTeam(team);
        }
    }

    //편의메서드
    private void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

}
