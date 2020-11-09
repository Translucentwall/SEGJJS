package edu.nju.se.teamnamecannotbeempty.data.domain;

import javax.persistence.*;
import java.util.Objects;

/**
 * 在迭代一和迭代二中作为会议PO，在迭代三中统一为出版物PO
 * 年份因为数据源扩展后数据无法统一，故不再作为论文的发表年份根据
 * 在论文PO中新增年份属性
 */
@Entity
@Table(name = "conferences")
public class Conference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "c_name")
    private String name;

    public Conference() {
    }

    @Override
    public String toString() {
        return "Conference{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Conference that = (Conference) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
