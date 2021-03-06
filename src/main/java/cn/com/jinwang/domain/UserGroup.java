package cn.com.jinwang.domain;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import cn.com.jinwang.factory.RepositoryFactoryHolder;
import cn.com.jinwang.interf.CanCopyProterties;
import cn.com.jinwang.interf.HasCreatedAt;
import cn.com.jinwang.interf.HasCreator;
import cn.com.jinwang.interf.HasDisplayLabel;
import cn.com.jinwang.interf.HasPermissionString;
import cn.com.jinwang.interf.HasSharedGroups;
import cn.com.jinwang.interf.HasSharedUsers;
import cn.com.jinwang.interf.Tree;
import cn.com.jinwang.misc.DomainUtils;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

import javax.persistence.JoinTable;

/**
 * Entity implementation class for Entity: ShareFileRole
 * 
 */
@Entity
@Table(name = "USER_GROUP")
public class UserGroup extends BaseTreeDomain<UserGroup>
    implements
      HasCreatedAt,
      HasCreator,
      Tree<UserGroup>,
      HasSharedUsers,
      HasSharedGroups,
      HasPermissionString,
      HasDisplayLabel,
      CanCopyProterties<UserGroup> {

  public static enum ActionEnum {
    MNG_MEMBER, UPDATE, DELETE, READ, ADD
  }

  private static final long serialVersionUID = 1L;

  public UserGroup() {}

  public UserGroup(String name) {
    this.name = name;
  }

  @Expose
  @Column(unique = true)
  private String name;

  @Expose
  private int position;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "creator_id", referencedColumnName = "id")
  private LocalUser creator;



  @Expose
  @Temporal(TIMESTAMP)
  private Date createdAt = new Date();

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(joinColumns = @JoinColumn(name = "UserGroup_id", referencedColumnName = "id"))
  private Set<LocalRole> roles = Sets.newHashSet();

  @ManyToMany(fetch = FetchType.LAZY)
  private Set<LocalUser> sharedUsers = Sets.newHashSet();

  @ManyToMany
  private Set<MixDomainPermission> permissions = Sets.newHashSet();

  @ManyToMany(fetch = FetchType.LAZY)
  private Set<UserGroup> sharedGroups = Sets.newHashSet();

  public int getDepth() {
    String[] ss = getParentIds().split(",");
    int i = 0;
    for (String s : ss) {
      if (s == null || s.trim().isEmpty()) {

      } else {
        i++;
      }
    }
    return i;
  }

  public Set<LocalRole> getRoles() {
    return roles;
  }

  public void setRoles(Set<LocalRole> roles) {
    this.roles = roles;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Set<MixDomainPermission> getPermissions() {
    return permissions;
  }

  public void setPermissions(Set<MixDomainPermission> permissions) {
    this.permissions = permissions;
  }

  public String getName() {
    return name;
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

  public Set<LocalUser> getSharedUsers() {
    return sharedUsers;
  }

  public void setSharedUsers(Set<LocalUser> sharedUsers) {
    this.sharedUsers = sharedUsers;
  }

  @Override
  public JsonObject toJsonObject(String jsonv) {
    return toJsonObject();
  }

  @Override
  public String toJson(String jsonv) {
    return toJsonObject(jsonv).toString();
  }

  public static Optional<UserGroup> find(long id) {
    return RepositoryFactoryHolder.getUserGroupRepository().findById(id);
  }

  public static void delete(UserGroup ug) {
    RepositoryFactoryHolder.getUserGroupRepository().delete(ug);
  }

  public UserGroup save() {
    return RepositoryFactoryHolder.getUserGroupRepository().save(this);
  }

  @Override
  public void copyProperties(UserGroup clientBean, String putsString) {
    setName(clientBean.getName());
  }

  public void addRole(LocalRole role) {
    getRoles().add(role);
    save();
  }

  public void removeRole(LocalRole role) {
    getRoles().remove(role);
    save();
  }

  // public boolean canMngMember(M3958SecurityUtil secUtil) {
  // return secUtil
  // .isAnyPermitted(getPermissionString(ActionEnum.MNG_MEMBER.toString()));
  // }
  //
  // public boolean canListArticle(M3958SecurityUtil secUtil) {
  // return
  // secUtil.isAnyPermitted(getPermissionString(Section.ActionEnum.LIST_ARTICLE.toString()));
  // }

  @Override
  public String[] getPermissionString(String... actions) {
    return DomainUtils.getPermissionString(this, actions);
  }

  @Override
  public List<MixDomainPermission> createAllMixPermissions() {
    List<MixDomainPermission> mps = Lists.newArrayList();

    // for (ActionEnum ae : ActionEnum.values()) {
    // if (ae == ActionEnum.ADD) continue;
    // MixDomainPermission mp =
    // new MixDomainPermission(this.getClass().getSimpleName(),
    // ae.toString(),
    // String.valueOf(getId()));
    // StaticEntityManagerHolder.getMpRepo().save(mp);
    // mps.add(mp);
    // }
    return mps;
  }

  @Override
  public MixDomainPermission getAddPermission() {
    return null;
  }

  public void addPermission(List<MixDomainPermission> mps) {
    getPermissions().addAll(mps);
    save();
  }

  public Set<UserGroup> getSharedGroups() {
    return sharedGroups;
  }

  public void setSharedGroups(Set<UserGroup> sharedGroups) {
    this.sharedGroups = sharedGroups;
  }

  @Override
  public boolean amIOwner(long userid) {
    return false;
  }

  @Override
  public Optional<UserGroup> findById(long id) {
    return RepositoryFactoryHolder.getUserGroupRepository().findById(id);
  }

  @Override
  public void setParentIds() {
    DomainUtils.setParentIds(this);
  }

  @Override
  public List<UserGroup> getBreadCrumb() {
    return DomainUtils.getBreadCrumb(this);
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  @Override
  public String getLabel() {
    return name;
  }

}
