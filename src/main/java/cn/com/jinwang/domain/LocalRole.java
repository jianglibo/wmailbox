package cn.com.jinwang.domain;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.UniqueConstraint;

import cn.com.jinwang.interf.CanCopyProterties;
import cn.com.jinwang.interf.HasCreatedAt;
import cn.com.jinwang.interf.HasCreator;
import cn.com.jinwang.misc.DomainUtils;

import com.google.common.base.Optional;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

import javax.persistence.JoinTable;
import javax.persistence.Column;

/**
 * Entity implementation class for Entity: ShareFileRole
 * 
 */
@Entity
@Table(name = "LOCAL_ROLE", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class LocalRole extends BaseDomain
    implements
      HasCreatedAt,
      HasCreator,
      CanCopyProterties<LocalRole> {

  private static final long serialVersionUID = 1L;

  @Expose
  @Temporal(TIMESTAMP)
  private Date createdAt;

  @Expose
  @Column(unique = true)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "creator_id", referencedColumnName = "id")
  private LocalUser creator;

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
  private Set<LocalUser> users;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "LOCAL_ROLE_SHARED_USERS")
  private Set<LocalUser> sharedUsers;

  public Set<LocalUser> getSharedUsers() {
    return sharedUsers;
  }

  public void setSharedUsers(Set<LocalUser> sharedUsers) {
    this.sharedUsers = sharedUsers;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalUser getCreator() {
    return creator;
  }

  public void setCreator(LocalUser creator) {
    this.creator = creator;
  }

  @Override
  public boolean amIOwner(long userid) {
    return DomainUtils.amIOwner(this, userid);
  }

  public Set<LocalUser> getUsers() {
    return users;
  }

  public void setUsers(Set<LocalUser> users) {
    this.users = users;
  }

  @Override
  public JsonObject toJsonObject(String jsonv) {
    return toJsonObject();
  }

  public void save() {
    // StaticEntityManagerHolder.getRoleRepo().save(this);
  }

  public static Optional<LocalRole> find(long id) {
    // return StaticEntityManagerHolder.getRoleRepo().findById(id);
    return null;
  }

  public static Optional<LocalRole> find(Optional<Long> idOp) {
    // return StaticEntityManagerHolder.getRoleRepo().findById(idOp);
    return null;
  }

  @Override
  public void copyProperties(LocalRole clientBean, String putString) {
    setName(clientBean.getName());
  }
}
