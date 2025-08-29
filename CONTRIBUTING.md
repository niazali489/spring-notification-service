# 🤝 Contributing to Spring Notification Service

First off, thank you for considering contributing to Spring Notification Service! It's people like you that make this
project a great tool for the developer community.

## 🌟 Ways to Contribute

- 🐛 **Bug Reports**: Help us identify and fix issues
- ✨ **Feature Requests**: Suggest new functionality
- 📝 **Documentation**: Improve or add documentation
- 💻 **Code Contributions**: Submit bug fixes or new features
- 🧪 **Testing**: Add or improve test coverage
- 🎨 **Design**: Improve UI/UX or create assets

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3.6+
- Git
- Your favorite IDE (IntelliJ IDEA recommended)

### Setup Development Environment

1. **Fork the repository**
   ```bash
   # Click the 'Fork' button on GitHub
   ```

2. **Clone your fork**
   ```bash
   git clone https://github.com/YOUR_USERNAME/spring-notification-service.git
   cd spring-notification-service
   ```

3. **Add upstream remote**
   ```bash
   git remote add upstream https://github.com/kenzycodex/spring-notification-service.git
   ```

4. **Install dependencies**
   ```bash
   mvn clean install
   ```

5. **Run tests**
   ```bash
   mvn test
   ```

## 📋 Development Guidelines

### Code Style

- Follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- Use meaningful variable and method names
- Add JavaDoc for public methods
- Keep methods small and focused (max 20-30 lines)

### Commit Messages

Follow [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

**Types:**

- `feat`: A new feature
- `fix`: A bug fix
- `docs`: Documentation only changes
- `style`: Code formatting changes
- `refactor`: Code changes that neither fix bugs nor add features
- `test`: Adding or modifying tests
- `chore`: Other changes (build process, auxiliary tools, etc.)

**Examples:**

```bash
git commit -m "feat(email): add HTML template support"
git commit -m "fix(websocket): resolve connection timeout issue"
git commit -m "docs(api): update authentication examples"
```

### Branch Naming

- `feature/feature-name` - New features
- `bugfix/bug-description` - Bug fixes
- `hotfix/critical-fix` - Critical fixes
- `docs/documentation-update` - Documentation updates

## 🔄 Pull Request Process

### Before Creating a PR

1. **Update your fork**
   ```bash
   git fetch upstream
   git checkout main
   git merge upstream/main
   ```

2. **Create feature branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **Make your changes**
    - Write clean, tested code
    - Follow existing patterns
    - Update documentation if needed

4. **Test your changes**
   ```bash
   mvn clean test
   mvn spring-boot:run
   # Manually test your changes
   ```

5. **Commit your changes**
   ```bash
   git add .
   git commit -m "feat: add your feature description"
   ```

6. **Push to your fork**
   ```bash
   git push origin feature/your-feature-name
   ```

### Creating the Pull Request

1. Go to your fork on GitHub
2. Click "New Pull Request"
3. Fill out the PR template:

```markdown
## Description

Brief description of changes made.

## Type of Change

- [ ] Bug fix
- [ ] New feature
- [ ] Documentation update
- [ ] Performance improvement
- [ ] Code refactoring

## Testing

- [ ] Tests pass locally
- [ ] New tests added (if applicable)
- [ ] Manual testing completed

## Screenshots (if applicable)

Add screenshots to help explain your changes.

## Checklist

- [ ] Code follows style guidelines
- [ ] Self-review completed
- [ ] Comments added for complex code
- [ ] Documentation updated
- [ ] No breaking changes (or clearly documented)
```

### PR Review Process

1. **Automated Checks**:
    - All tests must pass
    - Code coverage should not decrease
    - Build must be successful

2. **Code Review**:
    - At least one maintainer review required
    - Address all feedback before merging
    - Maintain constructive discussion

3. **Merge Requirements**:
    - All conversations resolved
    - No merge conflicts
    - Up-to-date with main branch

## 🐛 Bug Reports

### Before Submitting

- Check existing issues to avoid duplicates
- Test with the latest version
- Gather relevant information

### Bug Report Template

```markdown
**Describe the Bug**
A clear description of what the bug is.

**To Reproduce**
Steps to reproduce the behavior:

1. Go to '...'
2. Click on '....'
3. Scroll down to '....'
4. See error

**Expected Behavior**
What you expected to happen.

**Screenshots**
If applicable, add screenshots.

**Environment:**

- OS: [e.g. Windows 10, macOS 12.0]
- Java Version: [e.g. 17.0.2]
- Maven Version: [e.g. 3.8.4]
- Spring Boot Version: [e.g. 3.2.0]

**Additional Context**
Add any other context about the problem.
```

## ✨ Feature Requests

### Before Submitting

- Check if feature already exists
- Search existing feature requests
- Consider if it fits the project scope

### Feature Request Template

```markdown
**Is your feature request related to a problem?**
A clear description of what the problem is.

**Describe the solution you'd like**
A clear description of what you want to happen.

**Describe alternatives you've considered**
Any alternative solutions or features.

**Use Case**
Describe how this feature would be used.

**Additional context**
Add any other context or screenshots.
```

## 📝 Documentation Contributions

### Areas Needing Help

- API documentation improvements
- Code examples and tutorials
- Setup guides for different environments
- Troubleshooting guides
- Performance optimization tips

### Documentation Standards

- Use clear, concise language
- Include code examples
- Add screenshots where helpful
- Test all instructions
- Keep formatting consistent

## 🧪 Testing Guidelines

### Test Requirements

- All new features must include tests
- Bug fixes should include regression tests
- Aim for 80%+ code coverage
- Include both unit and integration tests

### Test Structure

```java

@ExtendWith(MockitoExtension.class)
class ServiceTest {

    @Mock
    private Dependency mockDependency;

    @InjectMocks
    private ServiceUnderTest service;

    @Test
    void shouldDoSomething_WhenConditionMet() {
        // Given
        // When  
        // Then
    }
}
```

### Test Naming Convention

- `shouldDoSomething_WhenConditionMet()`
- `shouldThrowException_WhenInvalidInput()`
- `shouldReturnExpectedResult_WhenValidInput()`

## 🚀 Development Workflow

### Daily Development

1. Pull latest changes from upstream
2. Create feature branch for your work
3. Make small, focused commits
4. Write tests for your changes
5. Update documentation as needed
6. Push changes and create PR

### Code Review Checklist

**For Authors:**

- [ ] Code is self-documenting
- [ ] Tests are included and passing
- [ ] Documentation is updated
- [ ] No debugging code left behind
- [ ] Performance implications considered

**For Reviewers:**

- [ ] Code follows project standards
- [ ] Logic is sound and efficient
- [ ] Edge cases are handled
- [ ] Security implications reviewed
- [ ] Breaking changes are documented

## 📞 Community

### Getting Help

- 🐛 **Issues**: For bugs and feature requests
- 💬 **Discussions**: For general questions and ideas
- 📧 **Email**: kenzycodex@gmail.com for private matters

### Community Guidelines

- Be respectful and inclusive
- Help newcomers get started
- Share knowledge and experience
- Provide constructive feedback
- Follow our Code of Conduct

## 🏆 Recognition

Contributors will be recognized in:

- README contributors section
- Release notes for major contributions
- Special mentions for outstanding work

### Types of Contributions Recognized

- **Code Contributors**: Bug fixes, features, improvements
- **Documentation Contributors**: Docs, examples, tutorials
- **Community Contributors**: Issue triage, helping others
- **Testing Contributors**: Test improvements, bug reports

## 📚 Resources

### Learning Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Reference](https://spring.io/projects/spring-security)
- [RabbitMQ Tutorials](https://www.rabbitmq.com/getstarted.html)
- [JWT.io](https://jwt.io/) - Learn about JSON Web Tokens

### Development Tools

- **IDE**: IntelliJ IDEA Community Edition (free)
- **API Testing**: Postman or Insomnia
- **Database**: MySQL Workbench or DBeaver
- **Git GUI**: SourceTree or GitKraken

## 🎯 Project Roadmap

### Current Focus Areas

- [ ] Performance optimizations
- [ ] Additional notification channels (SMS, Push)
- [ ] Enhanced monitoring and metrics
- [ ] GraphQL API support
- [ ] Kubernetes deployment examples

### Future Enhancements

- [ ] Notification templates system
- [ ] Advanced scheduling features
- [ ] Multi-tenant support
- [ ] Analytics and reporting
- [ ] Mobile SDKs

## 📋 Issue Labels

Understanding our label system:

**Type Labels:**

- `bug` - Something isn't working
- `enhancement` - New feature or request
- `documentation` - Improvements or additions to docs
- `question` - Further information is requested

**Priority Labels:**

- `priority/high` - High priority issues
- `priority/medium` - Medium priority issues
- `priority/low` - Low priority issues

**Status Labels:**

- `status/needs-triage` - Needs initial review
- `status/in-progress` - Currently being worked on
- `status/blocked` - Blocked by external dependency
- `status/ready-for-review` - Ready for code review

**Difficulty Labels:**

- `good first issue` - Good for newcomers
- `help wanted` - Extra attention is needed
- `difficulty/easy` - Easy to implement
- `difficulty/medium` - Moderate complexity
- `difficulty/hard` - Complex implementation

Thank you for contributing to Spring Notification Service! 🚀

---

**Questions?** Feel free to reach out by creating an issue or contacting [@kenzycodex](https://github.com/kenzycodex)