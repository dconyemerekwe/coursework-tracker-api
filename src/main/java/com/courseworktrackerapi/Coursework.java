package com.courseworktrackerapi;

import jakarta.persistence.*;
import java.time.LocalDate;

// data layer

@Entity // tells JPA/Hibernate that this class maps to a table in the DB
@Table(
    // specifies the exact name of that table
    name = "coursework",
    uniqueConstraints = {@UniqueConstraint(name = "title_unique", columnNames = "title")})
public class Coursework {

  @Id // defines the PK
  @SequenceGenerator(
      name = "coursework_sequence",
      sequenceName = "coursework_sequence",
      allocationSize = 1)
  // tells the DB how to handle numbering
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coursework_sequence")
  @Column(name = "id", updatable = false)
  private Long id;

  @Column(name = "title", updatable = true, nullable = false)
  private String title;

  @Column(name = "module_code", updatable = true, nullable = false)
  private String moduleCode;

  @Column(name = "due_date", updatable = true, nullable = false)
  private LocalDate dueDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", updatable = true, nullable = false)
  private Status status;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getModuleCode() {
    return moduleCode;
  }

  public void setModuleCode(String moduleCode) {
    this.moduleCode = moduleCode;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Coursework(Long id, String title, String moduleCode, LocalDate dueDate, Status status) {
    this.id = id;
    this.title = title;
    this.moduleCode = moduleCode;
    this.dueDate = dueDate;
    this.status = status;
  }

  public Coursework(Status status, LocalDate dueDate, String moduleCode, String title) {
    this.status = status;
    this.dueDate = dueDate;
    this.moduleCode = moduleCode;
    this.title = title;
  }

  public Coursework() {}

  @Override
  public String toString() {
    return "Coursework{"
        + "id="
        + id
        + ", title='"
        + title
        + '\''
        + ", moduleCode='"
        + moduleCode
        + '\''
        + ", dueDate="
        + dueDate
        + ", status="
        + status
        + '}';
  }
}
