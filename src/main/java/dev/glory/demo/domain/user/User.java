package dev.glory.demo.domain.user;

import static jakarta.persistence.EnumType.STRING;

import java.util.Collection;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import dev.glory.demo.domain.base.BaseEntity;
import dev.glory.demo.domain.user.role.Role;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.hibernate.type.YesNoConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
@SoftDelete(columnName = "deleted", strategy = SoftDeleteType.DELETED, converter = YesNoConverter.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noticeId", updatable = false)
    private Long id;

    @Column(length = 50, unique = true, nullable = false)
    @Comment("사용자 noticeId")
    private String username;

    @Column(nullable = false)
    @Comment("password")
    private String password;

    @Column(length = 50, nullable = false)
    @Comment("사용자 명")
    private String name;

    @Column(nullable = false)
    @Comment("사용자 이메일")
    private String email;

    @Column(nullable = false)
    @Convert(converter = YesNoConverter.class)
    @Comment("사용 여부")
    private boolean enabled;

    @Column(nullable = false)
    @Convert(converter = YesNoConverter.class)
    @Comment("잠금 여부")
    private boolean locked;

    @Column(nullable = false)
    @Enumerated(STRING)
    @Comment("권한")
    private Role role;


    @Builder
    private User(String username, String password, String name, String email, boolean enabled, boolean locked,
            Role role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.enabled = enabled;
        this.locked = locked;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

}
